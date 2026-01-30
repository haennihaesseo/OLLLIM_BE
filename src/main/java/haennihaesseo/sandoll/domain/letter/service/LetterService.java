package haennihaesseo.sandoll.domain.letter.service;

import haennihaesseo.sandoll.domain.deco.entity.Template;
import haennihaesseo.sandoll.domain.deco.entity.enums.Size;
import haennihaesseo.sandoll.domain.deco.repository.TemplateRepository;
import haennihaesseo.sandoll.domain.font.entity.Font;
import haennihaesseo.sandoll.domain.font.exception.FontException;
import haennihaesseo.sandoll.domain.font.repository.FontRepository;
import haennihaesseo.sandoll.domain.font.status.FontErrorStatus;
import haennihaesseo.sandoll.domain.letter.cache.CachedLetter;
import haennihaesseo.sandoll.domain.letter.cache.CachedLetterRepository;
import haennihaesseo.sandoll.domain.letter.cache.CachedWord;
import haennihaesseo.sandoll.domain.letter.converter.LetterConverter;
import haennihaesseo.sandoll.domain.letter.dto.request.LetterInfoRequest;
import haennihaesseo.sandoll.domain.letter.dto.response.WritingLetterContentResponse;
import haennihaesseo.sandoll.domain.letter.dto.response.VoiceSaveResponse;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import haennihaesseo.sandoll.global.infra.AwsS3Client;
import haennihaesseo.sandoll.global.infra.stt.GoogleSttClient;
import haennihaesseo.sandoll.global.infra.stt.SttResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetterService {

  private final AwsS3Client s3Client;
  private final GoogleSttClient googleSttClient;
  private final CachedLetterRepository cachedLetterRepository;
  private final FontRepository fontRepository;
  private final LetterConverter letterConverter;
  private final TemplateRepository templateRepository;

  /**
   * 음성 파일 저장 및 STT 편지 내용 조회, 편지 작성 키 발급
   * @param file
   * @return
   */
  public VoiceSaveResponse saveVoiceFile(MultipartFile file) {

    // 1. STT 처리 (S3 업로드 전에 처리)
    SttResult sttResult = googleSttClient.transcribe(file);

    // 10글자 이하일 경우 예외 처리
    if (sttResult.getFullText().replaceAll("\\s+", "").length() <= 10) {
      throw new LetterException(LetterErrorStatus.TOO_SHORT_CONTENT);
    }

    // 2. S3 업로드
    String fileUrl = s3Client.uploadFile("voice", file);

    // 3. Redis 저장
    String letterId = UUID.randomUUID().toString();
    List<CachedWord> cachedWords = letterConverter.toCachedWords(sttResult);
    CachedLetter cachedLetter = letterConverter.toCachedLetter(letterId, fileUrl, sttResult, cachedWords);
    cachedLetterRepository.save(cachedLetter);

    // 4. 응답 반환
    return letterConverter.toVoiceSaveResponse(letterId, fileUrl, sttResult);
  }

  /**
   * 편지 정보 입력 및 내용 수정
   * @param letterId
   * @param request
   */
  @Transactional
  public void inputLetterInfo(String letterId, LetterInfoRequest request) {
    // Redis에서 CachedLetter 조회
    CachedLetter cachedLetter = cachedLetterRepository.findById(letterId)
        .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

    // 편지 정보 업데이트
    cachedLetter.setInfo(request.getTitle(), request.getSender());

    // 편지 내용 비교 후 단어 업데이트
    String oldContent = cachedLetter.getContent();
    String newContent = request.getContent();

    if (!oldContent.equals(newContent)) {
      // 줄바꿈만 변경된 경우 단어 diff 생략
      String normalizedOld = oldContent.replaceAll("\\s+", " ").trim();
      String normalizedNew = newContent.replaceAll("\\s+", " ").trim();

      if (!normalizedOld.equals(normalizedNew)) {
        List<CachedWord> updatedWords = updateWords(cachedLetter.getWords(), oldContent, newContent);
        cachedLetter.setWords(updatedWords);
      }
      cachedLetter.setContent(newContent);
    }

    // Redis에 저장
    cachedLetterRepository.save(cachedLetter);
  }

  /**
   * 폰트 적용
   * @param letterId
   * @param fontId
   */
  @Transactional
  public void applyFont(String letterId, Long fontId) {
    // Redis에서 CachedLetter 조회
    CachedLetter cachedLetter = cachedLetterRepository.findById(letterId)
        .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

    // 폰트 존재 여부 확인
    Font font = fontRepository.findById(fontId)
        .orElseThrow(() -> new FontException(FontErrorStatus.FONT_NOT_FOUND));

    // 폰트 적용
    cachedLetter.setFont(font.getFontId(), font.getFontUrl());

    // 글자수 세기 -> 전체 글자수 + \n은 10자로 간주 (바뀔 수 있음)
    int charCount = 0;
    String content = cachedLetter.getContent();
    for (char c : content.toCharArray()) {
      if (c == '\n') {
        charCount += 10;
      } else {
        charCount += 1;
      }
    }

    Size size = Size.fromLength(charCount);
    //TODO:추후 템플릿 저장 후 주석 풀기, 현재는 디폴트 무지 템플릿으로 설정
//    Template setTemplate = templateRepository.findByNameAndSize("무지", size); // Default인 무지로 설정
//    cachedLetter.setTemplateId(setTemplate.getTemplateId());
    cachedLetter.setTemplateUrl("https://sandoll-s3-bucket.s3.ap-northeast-2.amazonaws.com/template/%E1%84%86%E1%85%AE%E1%84%8C%E1%85%B5.png");
    cachedLetterRepository.save(cachedLetter);
  }

  public WritingLetterContentResponse getWritingLetterContent(String letterId) {
    // Redis에서 CachedLetter 조회
    CachedLetter cachedLetter = cachedLetterRepository.findById(letterId)
        .orElseThrow(() -> new LetterException(LetterErrorStatus.LETTER_NOT_FOUND));

    // 폰트 이름 조회
    String fontName = null;
    if (cachedLetter.getFontId() != null) {
      fontName = fontRepository.findById(cachedLetter.getFontId())
          .map(Font::getName)
          .orElse(null);
    }

    return letterConverter.toWritingLetterContentResponse(cachedLetter, fontName);
  }

  private List<CachedWord> updateWords(List<CachedWord> existingWords, String oldContent, String newContent) {
    List<String> oldWords = Arrays.asList(oldContent.trim().split("\\s+"));
    List<String> newWords = Arrays.asList(newContent.trim().split("\\s+"));

    List<CachedWord> updatedWords = new ArrayList<>();

    int oldIdx = 0;
    int newIdx = 0;

    while (newIdx < newWords.size()) {
      String newWord = newWords.get(newIdx);

      if (oldIdx < oldWords.size() && oldIdx < existingWords.size()) {
        String oldWord = oldWords.get(oldIdx);
        CachedWord existingWord = existingWords.get(oldIdx);

        if (oldWord.equals(newWord)) {
          // 동일한 단어 - 그대로 유지
          updatedWords.add(existingWord);
          oldIdx++;
          newIdx++;
        } else if (newWords.subList(newIdx, newWords.size()).contains(oldWord)) {
          // 새 단어가 추가된 경우 (기존 단어가 뒤에 있음)
          double prevOrder = updatedWords.isEmpty() ? 0 : updatedWords.get(updatedWords.size() - 1).getWordOrder();
          double nextOrder = existingWord.getWordOrder();
          double newOrder = (prevOrder + nextOrder) / 2;

          CachedWord addedWord = CachedWord.builder()
              .word(newWord)
              .startTime(null)
              .endTime(null)
              .wordOrder(newOrder)
              .build();
          updatedWords.add(addedWord);
          newIdx++;
        } else if (oldWords.subList(oldIdx, oldWords.size()).contains(newWord)) {
          // 기존 단어가 삭제된 경우 (새 단어가 뒤에 있음)
          oldIdx++;
        } else {
          // 단어가 변경된 경우
          CachedWord updatedWord = CachedWord.builder()
              .word(newWord)
              .startTime(existingWord.getStartTime())
              .endTime(existingWord.getEndTime())
              .wordOrder(existingWord.getWordOrder())
              .build();
          updatedWords.add(updatedWord);
          oldIdx++;
          newIdx++;
        }
      } else {
        // 기존 단어가 모두 처리되었고, 새 단어가 남은 경우 (끝에 추가)
        double prevOrder = updatedWords.isEmpty() ? 0 : updatedWords.get(updatedWords.size() - 1).getWordOrder();
        double newOrder = prevOrder + 1;

        CachedWord addedWord = CachedWord.builder()
            .word(newWord)
            .startTime(null)
            .endTime(null)
            .wordOrder(newOrder)
            .build();
        updatedWords.add(addedWord);
        newIdx++;
      }
    }

    // 남은 기존 단어들은 삭제된 것
    while (oldIdx < oldWords.size() && oldIdx < existingWords.size()) {
      CachedWord deletedWord = existingWords.get(oldIdx);
      oldIdx++;
    }

    return updatedWords;
  }
}

