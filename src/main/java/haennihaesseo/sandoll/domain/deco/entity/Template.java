package haennihaesseo.sandoll.domain.deco.entity;

import haennihaesseo.sandoll.domain.deco.entity.enums.Size;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    private Size size;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}