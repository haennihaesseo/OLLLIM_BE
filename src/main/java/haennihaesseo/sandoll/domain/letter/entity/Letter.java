package haennihaesseo.sandoll.domain.letter.entity;

import haennihaesseo.sandoll.domain.deco.entity.Bgm;
import haennihaesseo.sandoll.domain.deco.entity.Envelope;
import haennihaesseo.sandoll.domain.deco.entity.Template;
import haennihaesseo.sandoll.domain.deco.entity.enums.ReceiverType;
import haennihaesseo.sandoll.domain.font.entity.Font;
import haennihaesseo.sandoll.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "letters")
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id", nullable = false)
    private Long letterId;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "voice_url", nullable = false)
    private String voiceUrl;

    @Column(name = "content", nullable = true, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = true)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_font_id", nullable = false)
    private Font defaultFont;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = true)
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bgm_id", nullable = true)
    private Bgm bgm;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "envelope_id", nullable = true)
    private Envelope envelope;

    @Column(name = "receiver_type", nullable = true)
    private ReceiverType receiverType;

    @Column(name = "voice_keyword", nullable = false)
    private String voiceKeyword;

    @Column(name = "context_keyword", nullable = false)
    private String contextKeyword;

    @Column(name = "created_at", nullable = false)
    @CurrentTimestamp
    private LocalDateTime createdAt;
}
