package projekti;

import java.time.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private FollowingMessageRepository msgFRepository;

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
//    @GetMapping("/{userId}"),@PathVariable Long userId
    @GetMapping("/myWall")
    public String userHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);

        List<Message> messages = user.getMessages();
        
        List<Followingship> followings = user.getFollowings();
        List<Followership> followers = user.getFollowers();
        //show following messages
        Pageable pageable = PageRequest.of(0, 25, Sort.by("time").descending());
        List<FollowingMessage> msgFList = this.msgFRepository.findByUser(user, pageable);
        
        
        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        model.addAttribute("followings", followings);
        model.addAttribute("followers", followers);
        model.addAttribute("msgFList", msgFList);
        return "user";
    }

    // post a new message
    @PostMapping("/myWall")
    public String saveNewMessage(@RequestParam(required = false) String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);

        if (content != null && !content.isEmpty()) {
            Message m = new Message();
            m.setComments(new ArrayList<>());
            m.setContent(content);
            m.setTime(LocalDateTime.now());
            m.setLikes(0);
            m.setUser(user);
            this.messageRepository.save(m);

            List<FollowingMessage> userMsgF = new ArrayList<>();
            for (Followership f : user.getFollowers()) {
                FollowingMessage msgF = new FollowingMessage();
                msgF.setContent(content);
                msgF.setTime(m.getTime());
                msgF.setWriterIdentity(user.getId());
                msgF.setWriterFamilyname(user.getFamilyname());
                msgF.setWriterFirstname(user.getFirstname());
                msgF.setMessageIdentity(m.getId());

                Account person = this.userRepository.getOne(f.getFollower());
                msgF.setUser(person);
                this.msgFRepository.save(msgF);
            }

            user.getMessages().add(m);
            System.out.println("MESSAGE: " + m.getContent());
            this.userRepository.save(user);
        }

        return "redirect:/myWall";
    }

    // show a comment on a message
//    @GetMapping("/myWall/messages/{messageId}/comments")
//    public String showComments(Model model, @PathVariable Long messageId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = this.userRepository.findByUsername(username);
//
////        model.addAttribute("user", user);
//        model.addAttribute("comments", this.commentRepository.findByMessageIdentity(messageId));
//        return "comment";
//    }
    // Now the messages are shown only on the userId's wall.
    // Which means when userId comment on followers' messages, it is ok to get userId as the write
    // If the comment is writing on userId's profile page, then it would be a problem.
    // Better way is to get writerId from the registered info. 
    // Correct this later!!!!!!!!!!!!!!!!!!!!
    // post a comment to the message
//    @PostMapping("/myWall/messages/{messageId}/comment") 
//    public String postComment(Model model, @RequestParam String comment, @PathVariable Long messageId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = this.userRepository.findByUsername(username);
//        
//        Comment c = new Comment();
//        Message m = this.messageRepository.getOne(messageId);
////        Account commentWriter = this.userRepository.findByMessageId(messageId);
////        Account messagePoster = user;
//        
//        c.setContent(comment);
//        c.setTime(LocalDateTime.now());
//        c.setMessage(m);
//        c.setWriter(user); // comment writer
//        c.setMessagePoster(user); //????? wall's owner
//      
//        this.commentRepository.save(c);
//        
//        return "redirect:/myWall";
//    }
    
    
//    @PostMapping("/myWall/messages/{messageId}/comments")
//    public String postCommentToOthers(Model model, @RequestParam String comment, @PathVariable Long messageId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        Account user = this.userRepository.findByUsername(username);
//
//        Comment c = new Comment();
////        FollowingMessage fm = this.msgFRepository.getOne(msgFId);
//        Message m = this.messageRepository.getOne(messageId);
//
//        c.setMessageIdentity(messageId);
//        c.setContent(comment);
//        c.setTime(LocalDateTime.now());
//        c.setWriter(user); // comment writer
//        
//        m.getComments().add(c);
//        
//        
////        c.setCommentMsgF(fm);
////        c.setMessage(m);
////        c.setMessagePoster(this.userRepository.getOne(fm.getWriterIdentity())); // message's poster
//        
//        
//        
//
//        this.commentRepository.save(c);
//        this.messageRepository.save(m);
//
//        return "redirect:/myWall/messages/" + messageId + "/comments";
//    }

    // likes of a message add up 
}
