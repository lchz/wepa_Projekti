
package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>{
//    @EntityGraph(attributePaths={"messages", "comments", "followers", "followings"})

    
    List<Account> findByFirstnameAndFamilyname(String firstname, String familyname);
    List<Account> findByFirstname(String firstname);
    List<Account> findByFamilyname(String familyname);
    Account findByUsername(String username);
}