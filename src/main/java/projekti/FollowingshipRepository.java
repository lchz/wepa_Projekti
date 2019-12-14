
package projekti;

import projekti.domain.Followingship;
import projekti.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingshipRepository extends JpaRepository<Followingship, Long>{
    Followingship findByUserAndFollowing(Account user, Long followingId);
}
