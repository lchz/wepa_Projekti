
package projekti;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/{userId}/follow/{personId}")
    public String follow(Model model, @PathVariable Long userId, @PathVariable Long personId) {
        // User is going to follow person
        User user = this.userRepository.getOne(userId); 
        User person = this.userRepository.getOne(personId);
        
        user.setDate(LocalDate.now());
        user.setTime(LocalTime.now());
        person.setDate(LocalDate.now());
        person.setTime(LocalTime.now());
        
        user.setFollower(person);
        this.userRepository.save(user);
        
        person.setFollowing(user);
        this.userRepository.save(person);
        
        return "redirect:/" + userId;
    }
    
}