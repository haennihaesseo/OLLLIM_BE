package haennihaesseo.sandoll.domain.deco.entity;

import haennihaesseo.sandoll.domain.letter.entity.Letter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "letter_sticker")
public class LetterSticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_sticker_id", nullable = false)
    private Long letterStickerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id", nullable = false)
    private Letter letter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sticker_id", nullable = false)
    private Sticker sticker;
    
    @Column(name = "pos_x", nullable = false)
    private Double posX;
    
    @Column(name = "pos_y", nullable = false)
    private Double posY;
}
