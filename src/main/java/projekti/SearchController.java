
package projekti;

import java.util.List;
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
    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }
    
    @PostMapping("/search")
    public String getFindingds(Model model, @RequestParam String firstname, @PathVariable String familyname) {
       
        if(firstname.isEmpty() && !familyname.isEmpty()) {
            
            List<User> familynames = this.userRepository.findByFamilyname(familyname);
            model.addAttribute("findings", familynames);
            
        } else if(familyname.isEmpty() && !firstname.isEmpty()) {
            
            List<User> firstnames = this.userRepository.findByFirstname(firstname);
            model.addAttribute("findings", firstnames);
            
        } else if(!familyname.isEmpty() && !firstname.isEmpty()) {    
            
            List<User> users = this.userRepository.findByFirstnameAndFamilyname(firstname, familyname);
            model.addAttribute("findings", users);
        }
        return "search";
    }
}
