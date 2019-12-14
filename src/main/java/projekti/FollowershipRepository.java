
package projekti;

import projekti.domain.Followership;
import projekti.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowershipRepository extends JpaRepository<Followership, Long>{
    Followership findByUserAndFollower(Account person, Long followerId);
}
