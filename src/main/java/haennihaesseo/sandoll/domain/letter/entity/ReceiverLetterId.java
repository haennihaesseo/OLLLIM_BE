package haennihaesseo.sandoll.domain.letter.entity;

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
public class ReceiverLetterId implements Serializable {

    @Column(name = "letter_id", nullable = false)
    private Long letterId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;
}
