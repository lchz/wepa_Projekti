
package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
//    @EntityGraph(attributePaths={"profilePic", "picAlbum", "messages", "comments", "followers", "followings"})
    List<User> findByFirstnameAndFamilyname(String first_name, String family_name);
    
}