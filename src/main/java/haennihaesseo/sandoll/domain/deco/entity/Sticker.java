package haennihaesseo.sandoll.domain.deco.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stickers")
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sticker_id", nullable = false)
    private Long stickerId;

    @Column(name = "sticker_url", nullable = false)
    private String stickerUrl;
}
