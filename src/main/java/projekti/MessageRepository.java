
package projekti;

import projekti.domain.Message;
import projekti.domain.Account;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {
//    Picture findByPictureId(Long picId);
    List<Message> findByUser(Account user, Pageable page);
    
}
