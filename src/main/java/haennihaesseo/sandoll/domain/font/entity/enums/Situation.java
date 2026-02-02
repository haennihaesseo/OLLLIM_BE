package haennihaesseo.sandoll.domain.font.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Situation {
    DAILY("일상의"), SPECIAL("특별한"), SERIOUS("진지한");

    private final String korean;
}
