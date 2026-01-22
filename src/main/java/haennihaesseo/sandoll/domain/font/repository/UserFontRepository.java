package haennihaesseo.sandoll.domain.font.repository;

import haennihaesseo.sandoll.domain.font.entity.UserFont;
import haennihaesseo.sandoll.domain.font.entity.UserFontId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFontRepository extends JpaRepository<UserFont, UserFontId> {
}
