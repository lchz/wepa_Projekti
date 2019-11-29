
package projekti;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends AbstractPersistable<Long>{
    private String content;
    private LocalDateTime time;

    @ManyToOne
    private Message message;   
    @ManyToOne
    private Picture picture;
    @OneToOne
    private Account writer;
    @OneToOne
    private Account messagePoster;
    
    @ManyToOne
    private FollowingMessage commentMsgF;
 
}
