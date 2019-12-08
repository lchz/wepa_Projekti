package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.service.CommentService;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    private String error;
    

    @GetMapping("/myWall/messages/{messageId}/comments")
    public String showComments(Model model, @PathVariable Long messageId) {

        model.addAttribute("comments", this.commentService.showComments(messageId));
        model.addAttribute("messageId", messageId);
        model.addAttribute("error", error);
        error="";

        return "comment";
    }

    @PostMapping("/myWall/messages/{messageId}/comments")
    public String postCommentToOthers(@RequestParam String comment, @PathVariable Long messageId, Model model) {

        if (comment.isEmpty()) {
            this.error = "The field must not be empty!";
            return "redirect:/myWall/messages/" + messageId + "/comments";
        }

        this.commentService.postComment(comment, messageId);
        return "redirect:/myWall/messages/" + messageId + "/comments";
    }
}
