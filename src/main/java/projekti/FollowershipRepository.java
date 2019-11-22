
package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowershipRepository extends JpaRepository<Followership, Long>{
    List<Followership> findByUser(User user);
    
}
