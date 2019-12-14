
package projekti.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingMessage extends AbstractPersistable<Long> {
    
    // user is following writer, user owns this relationship
    @ManyToOne
    private Account user;
    private Long messageIdentity;  // original message id
    @OneToOne
    private Picture picture;
    
    // message's writer
    @OneToOne
    private Account writer;
    
    private String content;
    private LocalDateTime time;
    
    private Integer likes;
}
