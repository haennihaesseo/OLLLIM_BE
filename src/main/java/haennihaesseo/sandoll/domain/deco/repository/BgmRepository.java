package haennihaesseo.sandoll.domain.deco.repository;

import haennihaesseo.sandoll.domain.deco.entity.Bgm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BgmRepository extends JpaRepository<Bgm, Long> {
}
