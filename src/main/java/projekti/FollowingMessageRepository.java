
package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FollowingMessageRepository extends JpaRepository<FollowingMessage, Long>{
    List<FollowingMessage> findByUser(Account user, Pageable page);
    List<FollowingMessage> findByMessageIdentity(Long messageId);
}
