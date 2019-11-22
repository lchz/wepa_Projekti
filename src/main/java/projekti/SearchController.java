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
    private UserRepository userRepository;
    @Autowired
    private FollowingshipRepository followingshipRepository;

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
        List<Followingship> followed = new ArrayList<>();

        if (firstname.isEmpty() && !familyname.isEmpty()) {

            List<User> familynames = this.userRepository.findByFamilyname(familyname);
            followed = this.followingshipRepository.findByFamilyname(familyname);

            for (int i = 0; i < followed.size(); i++) {
                User followingPerson = this.userRepository.getOne(followed.get(i).getFollowing());
                if (familynames.contains(followingPerson)) {
                    familynames.remove(followingPerson);
                }
            }

            model.addAttribute("findings", familynames);
            model.addAttribute("followed", followed);

        } else if (familyname.isEmpty() && !firstname.isEmpty()) {

            List<User> firstnames = this.userRepository.findByFirstname(firstname);
            followed = this.followingshipRepository.findByFirstname(firstname);

            for (int i = 0; i < followed.size(); i++) {
                User followingPerson = this.userRepository.getOne(followed.get(i).getFollowing());
                if (firstnames.contains(followingPerson)) {
                    firstnames.remove(followingPerson);
                }
            }

            model.addAttribute("findings", firstnames);
            model.addAttribute("followed", followed);

        } else if (!familyname.isEmpty() && !firstname.isEmpty()) {

            List<User> users = this.userRepository.findByFirstnameAndFamilyname(firstname, familyname);
            followed = this.followingshipRepository.findByFamilynameAndFirstname(familyname, firstname);

            for (int i = 0; i < followed.size(); i++) {
                User followingPerson = this.userRepository.getOne(followed.get(i).getFollowing());
                if (users.contains(followingPerson)) {
                    users.remove(followingPerson);
                }
            }

            model.addAttribute("findings", users);
            model.addAttribute("followed", followed);
        }

        return "search";
    }
}
