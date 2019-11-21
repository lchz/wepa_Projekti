
package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Followingship extends AbstractPersistable<Long>{
    @ManyToOne
    private User user;
    
    private Long following;
    private String fistname;
    private String familyname;
    private String username;
    private LocalDateTime time;
    
}
