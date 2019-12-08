
package projekti;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.service.FollowingService;

@Controller
@Profile({"production", "default"})
public class FollowingController {
    
    @Autowired
    private FollowingService followingService;
    
    
    @PostMapping("/myWall/follow/{personId}")
    @Transactional
    public String follow(Model model, @PathVariable Long personId) {
        
        this.followingService.following(personId);
        
        return "redirect:/myWall";
    }
    
    @PostMapping("myWall/deleteFollowing/{followingId}")
    @Transactional
    public String deleteFollowingship(@PathVariable Long followingId) {
        
        this.followingService.deleteFollowingship(followingId);
        
        return "redirect:/myWall";
    }
    
    @PostMapping("myWall/deleteFollower/{followerId}")
    @Transactional
    public String deleteFollowership(@PathVariable Long followerId) {
        
        this.followingService.deleteFollowership(followerId);
        
        return "redirect:/myWall";
    }
    
}