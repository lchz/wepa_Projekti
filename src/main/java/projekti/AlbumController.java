package projekti;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AlbumController {
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/myAlbum")    //@Valid, BindingResult bindingResult
    @Transactional
    public String saveNewPic(@RequestParam("file") MultipartFile file, @RequestParam String text) throws IOException {
//        if(bindingResult.hasErrors()) {
//            return "myAlbum";
//        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        
        Picture pi = new Picture();
        pi.setContent(file.getBytes());
        pi.setName(file.getOriginalFilename());
        pi.setMediaType(file.getContentType());
        pi.setSize(file.getSize());
        pi.setUser(user);
        
        Message m = new Message();
        m.setContent(text);
        m.setLikes(0);
        m.setTime(LocalDateTime.now());
        m.setUser(user);
        m.setPicture(pi);
        
        pi.setMessage(m);
        
        this.pictureRepository.save(pi);
        this.messageRepository.save(m);
        
        user.getPicAlbum().add(pi);
        this.userRepository.save(user);

        return "redirect:/myAlbum";
    }
    
// show the collection of pics and messages
    @GetMapping("/myAlbum")
    public String getAlbum(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        
        model.addAttribute("user", user);
        model.addAttribute("pictures", this.pictureRepository.findByUser(user));
        return "album";
    }
    
    @PostMapping("/myAlbum/delete/{picId}")
    @Transactional
    public String deletePic(@PathVariable Long picId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        
        Picture pic = this.pictureRepository.getOne(picId);
        
        this.commentRepository.deleteByMessageIdentity(pic.getMessage().getId());
        this.messageRepository.deleteById(pic.getMessage().getId());
        
        this.pictureRepository.deleteById(picId);
        
        return "redirect:/myAlbum";
    }
    
// show every pic
    @GetMapping(path = "/myAlbum/{id}", produces = "image/*")
    @ResponseBody
    public ResponseEntity<byte[]> viewPic(@PathVariable Long id) {
        Picture pi = this.pictureRepository.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pi.getMediaType()));
        headers.setContentLength(pi.getSize());
        headers.add("Content-Disposition", "filnename=" + pi.getName());
        
        return new ResponseEntity<>(pi.getContent(), headers, HttpStatus.CREATED);
        
    }
    
    // add comment to a pic
    @PostMapping("/myAlbum/messages/{messageId}/comments")
    public String addComment(@PathVariable Long messageId, @RequestParam String comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account user = this.userRepository.findByUsername(username);
        
        
        Comment c = new Comment();
        c.setContent(comment);
        c.setMessageIdentity(messageId);
        c.setTime(LocalDateTime.now());
        c.setWriter(user);
        
        this.commentRepository.save(c);
        
        this.messageRepository.getOne(messageId).getComments().add(c);
        
        return "redirect:comment";
    }
    
    // show comments of the pic
    @GetMapping("/myAlbum/messages/{messageId}/comments")
    public String showComments(Model model, @PathVariable Long messageId) {
        Pageable page = PageRequest.of(0, 10, Sort.by("time").descending());
        
        model.addAttribute("comments", this.commentRepository.findByMessageIdentity(messageId, page));
        model.addAttribute("messageId", messageId);
        
        return "comment";
    }
}
