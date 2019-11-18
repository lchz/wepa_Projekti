package projekti;


import java.time.*;
import java.util.*;
import javax.persistence.Entity;
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
    private User user;
    
    private String content;
    private LocalDate date;
    private LocalTime time;
    private long likes;
    
    @OneToMany(mappedBy="message")
    List<Comment> comments = new ArrayList<>();
}
