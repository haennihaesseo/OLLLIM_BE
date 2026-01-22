package haennihaesseo.sandoll.domain.deco.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "envelops")
public class Envelope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "envelope_id", nullable = false)
    private Long envelopeId;

    @Column(name = "envelope_url", nullable = false)
    private String envelopeUrl;
}
