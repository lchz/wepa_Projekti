
package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingshipRepository extends JpaRepository<Followingship, Long>{
    List<Followingship> findByFamilyname(String familyname);
    List<Followingship> findByFirstname(String firstname);
    List<Followingship> findByFamilynameAndFirstname(String familyname, String firstname);
}
