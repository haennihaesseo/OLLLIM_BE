package haennihaesseo.sandoll.domain.letter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LetterDetailResponse {
    private Long letterId;
    private String title;
    private String sender;
    private String content;
    private BgmInfo bgm;
    private TemplateInfo template;
    private FontInfo font;
    private VoiceInfo voice;
    private List<WordInfo> words;

    @Builder
    public record BgmInfo(Long bgmId, String bgmUrl) {}
    @Builder
    public record TemplateInfo(Long templateId, String templateUrl) {}
    @Builder
    public record FontInfo(Long fontId, String fontUrl) {}
    @Builder
    public record VoiceInfo(Long voiceId, String voiceUrl) {}
    @Builder
    public record WordInfo(Long wordId, String word, Double startTime, Double endTime) {}
}
