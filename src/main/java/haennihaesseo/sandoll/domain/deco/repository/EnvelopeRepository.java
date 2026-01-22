package haennihaesseo.sandoll.domain.deco.repository;

import haennihaesseo.sandoll.domain.deco.entity.Envelope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvelopeRepository extends JpaRepository<Envelope, Long> {
}
