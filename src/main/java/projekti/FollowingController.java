
package projekti;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowingController {
    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private FollowershipRepository followershipRepository;
    @Autowired
    private FollowingshipRepository followingshipRepository;
    @Autowired
    private FollowingMessageRepository msgFRepository;
    
    @PostMapping("/myWall/follow/{personId}")
    public String follow(Model model, @PathVariable Long personId) {
        // user is going to follow person
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        Account person = this.userRepository.getOne(personId);
        
        Followingship following = new Followingship();
        following.setFollowing(personId);
        following.setFamilyname(person.getFamilyname());
        following.setFirstname(person.getFirstname());
        following.setUsername(person.getUsername());
        following.setTime(LocalDateTime.now());
        following.setUser(user);
        user.getFollowings().add(following);
        this.followingshipRepository.save(following);System.out.println(personId);
        
        if(!person.getMessages().isEmpty()) {
            for(Message m: person.getMessages()) {
                FollowingMessage msgF = new FollowingMessage();
                msgF.setContent(m.getContent());
                msgF.setTime(m.getTime());
                msgF.setWriterIdentity(personId);
                msgF.setWriterFamilyname(person.getFamilyname());
                msgF.setWriterFirstname(person.getFirstname());
                msgF.setUser(user);
                msgF.setMessageIdentity(m.getId());

                user.getMsgF().add(msgF);

                this.msgFRepository.save(msgF);
            }
        }
        
        Followership follower = new Followership();
        follower.setFollower(user.getId());
        follower.setFamilyname(user.getFamilyname());
        follower.setFirstname(user.getFirstname());
        follower.setUsername(user.getUsername());
        follower.setTime(LocalDateTime.now());
        follower.setUser(person);
        person.getFollowers().add(follower);
        
        this.followershipRepository.save(follower);
        
        this.userRepository.save(user);
        this.userRepository.save(person);
        
        return "redirect:/myWall";
    }
    
    @PostMapping("myWall/deleteFollowing/{followingId}")
    @Transactional
    public String deleteFollowingship(@PathVariable Long followingId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        Account person = this.userRepository.getOne(followingId);
        
        Followingship f= this.followingshipRepository.findByUserAndFollowing(user, followingId);
        this.followingshipRepository.delete(f);
        
        Followership follower = this.followershipRepository.findByUserAndFollower(person, user.getId());
        this.followershipRepository.delete(follower);
        
        return "redirect:/myWall";
    }
    
    @PostMapping("myWall/deleteFollower/{followerId}")
    @Transactional
    public String deleteFollowership(@PathVariable Long followerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        Account person = this.userRepository.getOne(followerId);
        
        Followership follower = this.followershipRepository.findByUserAndFollower(user, followerId);
        this.followershipRepository.delete(follower);
        
        Followingship f = this.followingshipRepository.findByUserAndFollowing(person, user.getId());
        this.followingshipRepository.delete(f);
        
        return "redirect:/myWall";
    }
    
}