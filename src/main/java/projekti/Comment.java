
package projekti;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    private Message message;
    @OneToOne
    private User writer;
    @OneToOne
    private User messagePoster;
}
