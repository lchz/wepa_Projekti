
package projekti;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.*;
import org.hibernate.annotations.Type;
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
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] content;
    
}
