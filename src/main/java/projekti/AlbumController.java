package projekti;

import java.io.IOException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projekti.service.*;

@Controller
public class AlbumController {
    
    @Autowired
    private AccountService accountService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private ThumbService thumbService;
    @Autowired
    private CommentService commentService;
    private String error;
    

    @PostMapping("/myAlbum")
    @Transactional
    public String saveNewPic(@RequestParam("file") MultipartFile file, @RequestParam String text, Model model) throws IOException {
        
        error = this.albumService.saveNewPic(file, text);
        return "redirect:/myAlbum";
    }

    // show the collection of pics and messages
    @GetMapping("/myAlbum")
    public String getAlbum(Model model, @ModelAttribute Message m) {
        
        Account user = this.accountService.getUser();
        model.addAttribute("user", user);
        model.addAttribute("pictures",this.albumService.getPictures(user.getSignal()));
        model.addAttribute("error", error);
        error="";
        
        return "album";
        
    }

    @PostMapping("/myAlbum/delete/{picId}")
    @Transactional
    public String deletePic(@PathVariable Long picId) {
        
        this.albumService.deletePic(picId);
        return "redirect:/myAlbum";
        
    }
    

    // show every pic
    @GetMapping(path = "/myAlbum/{id}", produces = "image/*")
    @ResponseBody
    public ResponseEntity<byte[]> viewPic(@PathVariable Long id) {
        
        return this.albumService.viewPic(id);

    }

    // add comment to a pic
    @PostMapping("/myAlbum/messages/{messageId}/comments")
    @Transactional
    public String addComment(@PathVariable Long messageId,@RequestParam String comment) {
        
        this.commentService.postComment(comment, messageId);
        return "redirect:comment";
        
    }

    // show comments of the pic
    @GetMapping("/myAlbum/messages/{messageId}/comments")
    public String showComments(Model model, @PathVariable Long messageId) {

        model.addAttribute("user", this.accountService.getUser());
        model.addAttribute("comments", this.commentService.showComments(messageId));
        model.addAttribute("messageId", messageId);
        return "comment";
        
    }
    
    // like a pic on Album page
    @PostMapping("/myAlbum/likes/{messageId}")
    @Transactional
    public String likePic(@PathVariable Long messageId) {
        
        this.thumbService.addLike(messageId);
        return "redirect:/myAlbum";
        
    }
    
    @PostMapping("/myAlbum/profilePic/{picId}")
    @Transactional
    public String setProfilePic(@PathVariable Long picId) {
        
        this.albumService.setProfilePic(picId);
        return "redirect:/myAlbum";
        
    }
    
}
