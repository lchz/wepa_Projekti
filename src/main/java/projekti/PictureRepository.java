package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<Picture> findByUser(Account user, Pageable page);
    List<Picture> findByUser(Account user);

}
