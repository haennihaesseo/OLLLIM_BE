package haennihaesseo.sandoll.domain.font.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Distance {
    CLOSE("가까운"), NEUTRAL("적당한"), FAR("먼");

    private final String korean;
}
