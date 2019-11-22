
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

public class Followership extends AbstractPersistable<Long> {
    @ManyToOne
    private User user;
    
    private Long follower;
    private String firstname;
    private String familyname;
    private String username;
    private LocalDateTime time;
    
}