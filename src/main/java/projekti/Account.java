package projekti;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {
    @NotEmpty
    private String firstname;
    
    @NotEmpty
    private String familyname;
    
    @NotEmpty
    private String username;
    
    @NotEmpty
    private String password;
    
    @NotEmpty
    @Size(min=4, max=20)
    private String signal;
    
    @OneToOne
    private Picture profilePic;

    @OneToMany(mappedBy = "user")
    private List<Followingship> followings = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Followership> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Picture> picAlbum = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Message> messages = new ArrayList<>();

    // user wrote the comments
    @OneToMany(mappedBy = "writer")
    private List<Comment> comments = new ArrayList<>();
    
    @OneToMany(mappedBy="user")
    private List<FollowingMessage> msgF = new ArrayList<>();

}
