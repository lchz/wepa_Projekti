package projekti;

import projekti.domain.FollowingMessage;
import projekti.domain.Account;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingMessageRepository extends JpaRepository<FollowingMessage, Long> {

//    @EntityGraph(attributePaths = {"picture", "writer"})
    List<FollowingMessage> findByUser(Account user, Pageable page);

    List<FollowingMessage> findByMessageIdentity(Long messageId);

    void deleteByMessageIdentity(Long messageId);

    void deleteByUserAndWriter(Account user, Account writerIdentity);
}
