package projekti;

import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CommentRepository commentRepository;
    

/*    // Get profile picture
    @GetMapping(path = "/{userId}")
    public ResponseEntity<byte[]> viewProfilePicture(@PathVariable Long userId) {
        Picture picture = this.userRepository.getOne(userId).getProfilePic();
        
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(picture.getMediaType()));
        headers.setContentLength(picture.getSize());
        
        return new ResponseEntity<>(picture.getContent(), headers, HttpStatus.CREATED);
    } */

// Get profile picture
    @GetMapping(path = "/{userId}", produces = "image/*")
    @ResponseBody
    public byte[] get(@PathVariable Long userId) {
        return this.userRepository.getOne(userId).getProfilePic().getContent();	
    }
    
    // Get user's name, username, messages
    @GetMapping("/{userId}")
    public String userHome(Model model, @PathVariable Long userId) {
        User user = this.userRepository.getOne(userId);
        List<Message> m = user.getMessages();
        
        model.addAttribute("user", user);
        
        if(m.isEmpty()) {
            model.addAttribute("messages", null); 
        } else {
            model.addAttribute("messages", m); 
        }
        
        return "user";
    }
 
    // post a new message
    @PostMapping("/{userId}")
    public String saveNewMessage(@RequestParam String content, @PathVariable Long userId) {
        Message m = new Message();
        m.setComments(new ArrayList<>());
        m.setContent(content);
        m.setDate(LocalDate.now());
        m.setTime(LocalTime.now());
        m.setLikes(0);
        m.setUser(this.userRepository.getOne(userId));
        
        this.messageRepository.save(m);
        
        return "redirect:/{userId}";
    }
    
    
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
 /*   
    // search a user
    @PostMapping("/search/{firstname}/{familyname}")
    public String searchUser(@RequestParam String firstname, @RequestParam String familyname) {
        if(firstname.isEmpty()) {
            firstname = "-";
        } else if(familyname.isEmpty()) {
            familyname = "-";
        }
        return "redirect:/" + firstname + "/" + familyname;
    }
 */   
    // find the user
    @GetMapping("/{firstname}/{familyname}")
    public String find(Model model, @PathVariable String firstname, @PathVariable String familyname) {
        List<User> users =  this.userRepository.findByFirstnameAndFamilyname(firstname, familyname);
        
        model.addAttribute("findings", users);
        return "search";
    }
    
    // show a comment to a message
    @GetMapping("/{userId}/messages/{messageId}/comment")
    public String addComment(Model model, @PathVariable Long userId, @PathVariable Long messageId) {
        model.addAttribute("user", this.userRepository.getOne(userId));
        model.addAttribute("message", this.messageRepository.getOne(messageId));
        return "comment";
    }
    
    // Now the messages are shown only on the userId's wall.
    // Which means when userId comment on followers' messages, it is ok to get userId as the write
    // If the comment is writing on userId's profile page, then it would be a problem.
    // Better way is to get writerId from the registered info. 
    // Correct this later!!!!!!!!!!!!!!!!!!!!
    // post a comment to the message
    @PostMapping("/{userId}/messages/{messageId}/comment") 
    public String postComment(Model model, @RequestParam String comment, @PathVariable Long userId, @PathVariable Long messageId) {
        Comment c = new Comment();
        Message m = this.messageRepository.getOne(messageId);
        User w = this.userRepository.getOne(userId);
        User mp = this.userRepository.getOne(userId);
        
        c.setContent(comment);
        c.setDate(LocalDate.now());
        c.setTime(LocalTime.now());
        c.setMessage(m);
        c.setWriter(w);
        c.setMessagePoster(mp);
      
        this.commentRepository.save(c);
        
        return "redirect:/{userId}";
    }
    
    // likes of a message add up 

}
