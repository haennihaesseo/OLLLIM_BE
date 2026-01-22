package haennihaesseo.sandoll.domain.deco.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bgms")
public class Bgm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bgm_id", nullable = false)
    private Long bgmId;

    @Column(name = "bgm_url", nullable = false)
    private String bgmUrl;

    @Column(name = "name", nullable = false)
    private String name;
}
