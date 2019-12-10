package projekti;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projekti.service.AccountService;
import projekti.service.ThumbService;

@Controller
@Profile({"production", "default"})
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ThumbService thumbService;

    private String error;

    
    // Get user's name, messages, followings, followers, following messages
    @GetMapping("/myWall")
    public String userHome(Model model) {

        model.addAttribute("user", this.accountService.getUser());
        model.addAttribute("followings", this.accountService.getFollowings());
        model.addAttribute("followers", this.accountService.getFollowers());
        model.addAttribute("msgFList", this.accountService.getFollowingMessages());
        model.addAttribute("error", error);
        error = "";

        return "user";
    }

    // post a new message
    @PostMapping("/myWall")
    @Transactional
    public String saveNewMessage(@ModelAttribute Message message) {
        
        if(message.getContent() != null) {
            error = this.accountService.postNewMessage(message);
        }
        
        return "redirect:/myWall";
    }

    // like a message
    @PostMapping("/myWall/likes/{messageId}")
    @Transactional
    public String addALike(@PathVariable Long messageId) {
        
        this.thumbService.addLike(messageId);
        return "redirect:/myWall";
        
    }

    // to other's wall
    @GetMapping("/profile/{signal}")
    public String goToVisit(Model model, @PathVariable String signal) {
        
        model.addAttribute("user", this.accountService.getUser(signal));
        model.addAttribute("messages", this.accountService.getMessages(signal));
        model.addAttribute("followings", this.accountService.getFollowings());
        model.addAttribute("followers", this.accountService.getFollowers());
        model.addAttribute("msgFList", this.accountService.getFollowingMessages());

        return "visit";
    }
    
    @GetMapping("/myPosts") 
    public String getPosts(Model model) {
        
        model.addAttribute("user", this.accountService.getUser());
        model.addAttribute("messages", this.accountService.getMessages(this.accountService.getUser().getSignal()));
        return "posts";
        
    }
    
    // to my profile
    @GetMapping("/boot")
    public String goToProfile() {
        return "bootstrap";
    }
}
