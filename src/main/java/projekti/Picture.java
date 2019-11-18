
package projekti;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture extends AbstractPersistable<Long>{
    private String text;
    private Boolean profilePic;
    
    @ManyToOne
    private User user;
    
    private String name;
    private String mediaType;
    private Long size;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;
    

}
