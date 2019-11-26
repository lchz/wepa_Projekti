
package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingMessage extends AbstractPersistable<Long>{
    // user is following writer, user owns this relationship
    @ManyToOne
    private User user;
    
    private Long writerIdentity;
    private String writerFirstname;
    private String writerFamilyname;
    private String content;
    private LocalDateTime time;
}
