
package projekti;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    
    // go to search page
    @GetMapping("/{userId}/search")
    public String searchPage(Model model, @PathVariable Long userId) {
        User user = this.userRepository.getOne(userId);
        model.addAttribute("user", user);
        return "search";
    }
    
    @PostMapping("/{userId}/search")
    public String getFindings(Model model, @RequestParam String firstname, @RequestParam String familyname, @PathVariable Long userId) {
        User user = this.userRepository.getOne(userId);
        model.addAttribute("user", user);
//        List<User> followed = new ArrayList<>();
        
        if(firstname.isEmpty() && !familyname.isEmpty()) {
            
            List<User> familynames = this.userRepository.findByFamilyname(familyname);
            
//            familynames.removeAll(followed);
            model.addAttribute("findings", familynames);
            
        } 
        else if(familyname.isEmpty() && !firstname.isEmpty()) {
            
            List<User> firstnames = this.userRepository.findByFirstname(firstname);
//            List<User> followed = user.getFollowings();

//            firstnames.removeAll(followed);
            model.addAttribute("findings", firstnames);
//            model.addAttribute("followed", followed);
            
        } else if(!familyname.isEmpty() && !firstname.isEmpty()) {    
            
            List<User> users = this.userRepository.findByFirstnameAndFamilyname(firstname, familyname);
//            List<User> followed = user.getFollowings();

//            users.removeAll(followed);
            model.addAttribute("findings", users);
//            model.addAttribute("followed", followed);
        }
        
        
        return "search";
    }
}