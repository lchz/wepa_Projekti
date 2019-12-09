package projekti;

import java.io.IOException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projekti.service.AccountService;
import projekti.service.AlbumService;

@Controller
public class AlbumController {
    
    @Autowired
    private AccountService accountService;
    @Autowired
    private AlbumService albumService;
    

    @PostMapping("/myAlbum")
    @Transactional
    public String saveNewPic(@RequestParam("file") MultipartFile file, @RequestParam String text, Model model) throws IOException {
        
        if(text.isEmpty()) {
            model.addAttribute("error", "The description must not be empty!");
            return "album";
        }
        
        if(file.getSize() == 0) {
            model.addAttribute("error", "Picture does not exist!");
            return "album";
        }

        this.albumService.saveNewPic(file, text);
        return "redirect:/myAlbum";
    }

// show the collection of pics and messages
    @GetMapping("/myAlbum")
    public String getAlbum(Model model, @ModelAttribute Message m) {
        
        Account user = this.accountService.getUser();
        model.addAttribute("user", user);
        model.addAttribute("pictures",this.albumService.getPictures(user));
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
    public String addComment(@PathVariable Long messageId,@RequestParam String comment) {
        
        this.albumService.addComment(messageId, comment);
        return "redirect:comment";
        
    }

    // show comments of the pic
    @GetMapping("/myAlbum/messages/{messageId}/comments")
    public String showComments(Model model, @PathVariable Long messageId) {

        model.addAttribute("comments", this.albumService.showComments(messageId));
        model.addAttribute("messageId", messageId);
        return "comment";
        
    }
}
