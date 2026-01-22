package haennihaesseo.sandoll.domain.letter.repository;

import haennihaesseo.sandoll.domain.letter.entity.ReceiverLetter;
import haennihaesseo.sandoll.domain.letter.entity.ReceiverLetterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverLetterRepository extends JpaRepository<ReceiverLetter, ReceiverLetterId> {
}
