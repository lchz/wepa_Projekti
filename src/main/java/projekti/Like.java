
package projekti;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like extends AbstractPersistable<Long>{
    @ManyToOne
    private Message message;
    
    @OneToOne
    private Account user;
    private Long messageIdentity;
}
