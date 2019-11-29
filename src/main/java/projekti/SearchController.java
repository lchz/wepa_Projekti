package projekti;

import java.util.ArrayList;
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
    private AccountRepository userRepository;
    @Autowired
    private FollowingshipRepository followingshipRepository;

    // go to search page
    @GetMapping("/{userId}/search")
    public String searchPage(Model model, @PathVariable Long userId) {

        Account user = this.userRepository.getOne(userId);
        model.addAttribute("user", user);
        
        return "search";
    }

    @PostMapping("/{userId}/search")
    public String getFindings(Model model, @RequestParam String firstname, @RequestParam String familyname, @PathVariable Long userId) {

        Account user = this.userRepository.getOne(userId);
        List<Followingship> followed = user.getFollowings();
        List<Account> users = new ArrayList<>();

        if (firstname.isEmpty() && !familyname.isEmpty()) {
            users = this.userRepository.findByFamilyname(familyname);
        } else if (familyname.isEmpty() && !firstname.isEmpty()) {
            users = this.userRepository.findByFirstname(firstname);
        } else if (!familyname.isEmpty() && !firstname.isEmpty()) {
            users = this.userRepository.findByFirstnameAndFamilyname(firstname, familyname);
        }

        for (int i = 0; i < followed.size(); i++) {
            Account followingPerson = this.userRepository.getOne(followed.get(i).getFollowing());
            if (users.contains(followingPerson)) {
                users.remove(followingPerson);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("findings", users);
        model.addAttribute("followed", followed);

        return "search";
    }
}
