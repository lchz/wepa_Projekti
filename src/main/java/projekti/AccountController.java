package projekti;

import java.time.*;
import java.util.*;
import javax.transaction.Transactional;
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
    @Autowired
    private ThumbUpRepository likeRepository;

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
    @Transactional
    public String saveNewMessage(@RequestParam(required = false) String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);

        if (content != null && !content.isEmpty()) {
            Message m = new Message();
            m.setComments(new ArrayList<>());
            m.setContent(content);
            m.setTime(LocalDateTime.now());
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
                msgF.setLikes(0);
                msgF.setWithPic(false);

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
    
    // like a message
    @PostMapping("/myWall/likes/{messageId}")
    @Transactional
    public String addALike(@PathVariable Long messageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        Message m = this.messageRepository.getOne(messageId);

        if(this.likeRepository.existsByUserAndMessage(user, m)) {
            return "redirect:/myWall";
        }
        
        ThumbUp like = new ThumbUp();
        like.setUser(user);
        like.setMessage(m);
        
        m.getLikes().add(like);
        
        FollowingMessage fm = this.msgFRepository.findByMessageIdentityAndUser(messageId, user);
        if(fm != null) {
            fm.setLikes(m.getLikes().size());
            this.msgFRepository.save(fm);
        }
        
        this.likeRepository.save(like);
        this.messageRepository.save(m);
        
        return "redirect:/myWall";
    }
}
