
package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FollowController {
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/{userId}/search/{id}")
    @Transactional
    public String follow(Model model, @PathVariable Long userId, @PathVariable Long id) {
        User user = this.userRepository.getOne(userId); 
        User following = this.userRepository.getOne(id);
        
        user.getFollowers().add(following);
        return "/{userId}/search/{id}";
    }
    
    @GetMapping("/{userId}/search/{id}") 
    public String goToFollowingPage(Model model, @PathVariable Long userId, @PathVariable Long id) {
        User user = this.userRepository.getOne(userId); 
        User following = this.userRepository.getOne(id);
        return "following";
    }
}
