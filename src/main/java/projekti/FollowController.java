
package projekti;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowershipRepository followershipRepository;
    @Autowired
    private FollowingshipRepository followingshipRepository;
    
    @PostMapping("/{userId}/follow/{personId}")
    public String follow(Model model, @PathVariable Long userId, @PathVariable Long personId) {
        // User is going to follow person
        User user = this.userRepository.getOne(userId); 
        User person = this.userRepository.getOne(personId);
        
        Followingship following = new Followingship();
        following.setFollowing(personId);
        following.setFamilyname(person.getFamilyname());
        following.setFirstname(person.getFirstname());
        following.setUsername(person.getUsername());
        following.setTime(LocalDateTime.now());
        following.setUser(user);
        user.getFollowings().add(following);
        
        this.followingshipRepository.save(following);
        
        Followership follower = new Followership();
        follower.setFollower(userId);
        follower.setFamilyname(user.getFamilyname());
        follower.setFirstname(user.getFirstname());
        follower.setUsername(user.getUsername());
        follower.setTime(LocalDateTime.now());
        follower.setUser(person);
        person.getFollowers().add(follower);
        
        this.followershipRepository.save(follower);
        
        this.userRepository.save(user);
        this.userRepository.save(person);
        
        return "redirect:/" + userId;
    }
    
}