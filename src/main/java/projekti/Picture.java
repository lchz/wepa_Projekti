
package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture extends AbstractPersistable<Long>{
    @OneToOne
    private Message message;
    private Boolean profilePic;
    
    @ManyToOne
    private Account user;
    
    private String name;
    private String mediaType;
    private Long size;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
    
//    @OneToMany
//    private List<Comment> comments = new ArrayList<>();
    

}
