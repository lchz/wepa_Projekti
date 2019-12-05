
package projekti;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>{
//    Optional<Like> findByUserAndMessageIdentity(Account user, Long mId);
    Boolean existsByUserAndMessage(Account user, Message m);
    
}
