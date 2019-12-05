package projekti;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository userRepository;

    @GetMapping("/myWall/messages/{messageId}/comments")
    public String showComments(Model model, @PathVariable Long messageId) {
        Pageable page = PageRequest.of(0, 10, Sort.by("time").descending());

        model.addAttribute("comments", this.commentRepository.findByMessageIdentity(messageId, page));
        model.addAttribute("messageId", messageId);
        return "comment";
    }

    @PostMapping("/myWall/messages/{messageId}/comments")
    public String postCommentToOthers(Model model, @RequestParam String comment, @PathVariable Long messageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);

        Comment c = new Comment();

        c.setMessageIdentity(messageId);
        c.setContent(comment);
        c.setTime(LocalDateTime.now());
        c.setWriter(user); // comment writer

        Message m = this.messageRepository.getOne(messageId);
        m.getComments().add(c);

        this.commentRepository.save(c);
        this.messageRepository.save(m);

        return "redirect:/myWall/messages/" + messageId + "/comments";
    }
}
