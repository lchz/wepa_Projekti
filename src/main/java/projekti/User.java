
package projekti;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractPersistable<Long>{
    private String firstname;
    private String familyname;
    private String username;
    private String password;
    @OneToOne
    private Picture profilePic;
    
    private LocalDate date;
    private LocalTime time;
    
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="follower_id")
    private User follower;
    
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="following_id")
    private User following;
    
    @OneToMany(mappedBy="follower")
    private List<User> followers = new ArrayList<>();
    @OneToMany(mappedBy="following")
    private List<User> followings = new ArrayList<>();
    
    @OneToMany(mappedBy="user")
    private List<Picture> picAlbum = new ArrayList<>();
    
    @OneToMany(mappedBy="user")
    private List<Message> messages = new ArrayList<>();
    
    @OneToMany(mappedBy="messagePoster")
    private List<Comment> comments = new ArrayList<>();
    
}
