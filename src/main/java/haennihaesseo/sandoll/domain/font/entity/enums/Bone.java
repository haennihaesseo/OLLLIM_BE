package haennihaesseo.sandoll.domain.font.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Bone {
    ROUND("둥근"), NATURAL("자연스러운"), ANGULAR("각진");

    private final String korean;
}
