package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.service.AccountService;
import projekti.service.SearchService;

@Controller
@Profile({"production", "default", "test"})
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    @Autowired
    private AccountService accountService;

    
    @GetMapping("/search")
    public String searchPage(Model model) {

        model.addAttribute("user", this.accountService.getUser());

        return "search";
    }

    @PostMapping("/search")
    public String getFindings(Model model, @RequestParam String firstname, @RequestParam String familyname) {

        model.addAttribute("user", this.accountService.getUser());
        model.addAttribute("findings", this.searchService.foundUsers(firstname, familyname));
        model.addAttribute("followed", this.searchService.foundFollowing());

        return "search";
    }
}
