package projekti;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import projekti.service.AccountService;
import projekti.service.ThumbService;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ThumbService thumbService;

    @Autowired
    private PictureRepository pictureRepository;
    private String error;

// Get profile picture
//    @GetMapping(path = "/myWall", produces = "image/*")
//    @ResponseBody
//    public byte[] get() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = this.userRepository.findByUsername(username);
//        
//        return user.getProfilePic().getContent();	
//    }
    // Get user's name, messages, followings, followers, following messages
    @GetMapping("/myWall")
    public String userHome(Model model) {

        model.addAttribute("user", this.accountService.getUser());
        model.addAttribute("messages", this.accountService.getMessages());
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
    public String saveNewMessage(@RequestParam(required=false) String content) {
        if(content.isEmpty()) {
            this.error = "The field must not be empty!";
            return "redirect:/myWall";
        }
        
        this.accountService.postNewMessage(content);
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
        model.addAttribute("messages", this.accountService.getMessages());
        model.addAttribute("followings", this.accountService.getFollowings());
        model.addAttribute("followers", this.accountService.getFollowers());
        model.addAttribute("msgFList", this.accountService.getFollowingMessages());

        return "visit";
    }
    
    // to my profile
    @GetMapping("/myProfile/{username}/{signal}")
    public String goToProfile() {
        return "profile";
    }
}
