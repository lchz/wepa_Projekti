
package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbUpRepository extends JpaRepository<ThumbUp, Long>{
    
    Boolean existsByUserAndMessage(Account user, Message m);
    void deleteByMessage(Message message);
    
}
