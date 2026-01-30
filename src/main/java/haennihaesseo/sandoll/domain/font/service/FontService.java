package haennihaesseo.sandoll.domain.font.service;

import haennihaesseo.sandoll.domain.deco.entity.enums.Size;
import haennihaesseo.sandoll.domain.font.entity.Font;
import haennihaesseo.sandoll.domain.font.exception.FontException;
import haennihaesseo.sandoll.domain.font.repository.FontRepository;
import haennihaesseo.sandoll.domain.font.status.FontErrorStatus;
import haennihaesseo.sandoll.domain.letter.cache.CachedLetter;
import haennihaesseo.sandoll.domain.letter.cache.CachedLetterRepository;
import haennihaesseo.sandoll.domain.letter.dto.response.WritingLetterContentResponse;
import haennihaesseo.sandoll.domain.letter.exception.LetterException;
import haennihaesseo.sandoll.domain.letter.status.LetterErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FontService {

  private final CachedLetterRepository cachedLetterRepository;
  private final FontRepository fontRepository;

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

}
