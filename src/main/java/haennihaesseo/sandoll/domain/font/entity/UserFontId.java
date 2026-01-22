package haennihaesseo.sandoll.domain.font.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserFontId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "font_id", nullable = false)
    private Long fontId;
}
