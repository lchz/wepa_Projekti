
package projekti;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbUpRepository extends JpaRepository<ThumbUp, Long>{
//    Optional<Like> findByUserAndMessageIdentity(Account user, Long mId);
    Boolean existsByUserAndMessage(Account user, Message m);
    void deleteByMessage(Message message);
    
}
