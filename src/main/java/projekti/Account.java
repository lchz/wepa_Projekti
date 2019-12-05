package projekti;

import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//customer, account
public class Account extends AbstractPersistable<Long> {

    private String firstname;
    private String familyname;
    private String username;
    private String password;
    @OneToOne
    private Picture profilePic;

    @OneToMany(mappedBy = "user")
    private List<Followingship> followings = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Followership> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
//    @Size(max = 3)
    private List<Picture> picAlbum = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Message> messages = new ArrayList<>();

    // user wrote the comments
    @OneToMany(mappedBy = "writer")
    private List<Comment> comments = new ArrayList<>();
    
    @OneToMany(mappedBy="user")
    private List<FollowingMessage> msgF = new ArrayList<>();


}
