package haennihaesseo.sandoll.domain.user.repository;

import haennihaesseo.sandoll.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
