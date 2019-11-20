
package projekti;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FollowController {
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping(value="/{userId}/search/{personId}")
//    @Transactional
    public String follow(Model model, @PathVariable Long userId, @PathVariable Long personId) {
        User user = this.userRepository.getOne(userId); 
        User follower = this.userRepository.getOne(personId);
        
        user.setDate(LocalDate.now());
        user.setTime(LocalTime.now());
        follower.setDate(LocalDate.now());
        follower.setTime(LocalTime.now());
        
        user.getFollowers().add(follower);
        follower.getFollowings().add(user);
        
        this.userRepository.save(user);
        this.userRepository.save(follower);
        
        return "redirect: /{userId}/search";
    }
    
//    @GetMapping("/{userId}/search/{id}") 
//    public String goToFollowingPage() {
//        User user = this.userRepository.getOne(userId); 
//        User following = this.userRepository.getOne(id);
//        return "";
//    }
}