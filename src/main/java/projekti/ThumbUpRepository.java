
package projekti;

import projekti.domain.Message;
import projekti.domain.ThumbUp;
import projekti.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbUpRepository extends JpaRepository<ThumbUp, Long>{
    
    Boolean existsByUserAndMessage(Account user, Message m);
    void deleteByMessage(Message message);
    
}
