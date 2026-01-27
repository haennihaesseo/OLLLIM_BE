package haennihaesseo.sandoll.domain.letter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SendLetterResponse {
    private Long letterId;
    private String title;
    private LocalDate createdAt;
}
