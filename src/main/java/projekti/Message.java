package projekti;


import java.time.*;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractPersistable<Long> {
    @ManyToOne
    private Account user;
    
    private String content;
    private LocalDateTime time;
    private long likes;
    
    @OneToMany(mappedBy="message")
    private List<Comment> comments = new ArrayList<>();
    
}
