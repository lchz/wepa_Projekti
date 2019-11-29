
package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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

public class Followership extends AbstractPersistable<Long> {
    @ManyToOne
    private Account user;
    
    private Long follower;
    private String firstname;
    private String familyname;
    private String username;
    private LocalDateTime time;
    
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name="msgF_msg",
//            joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
//            inverseJoinColumns=@JoinColumn(name="message_id", referencedColumnName="id"))
//    private List<Message> msgF = new ArrayList<>();
    
}