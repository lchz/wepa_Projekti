
package projekti.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends AbstractPersistable<Long> {
    
    private String content;
    private LocalDateTime time;

    private Long messageIdentity;   
    
    @ManyToOne
    private Account writer;

}
