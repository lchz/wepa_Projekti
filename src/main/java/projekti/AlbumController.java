package projekti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AlbumController {
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private AccountRepository userRepository;

    @PostMapping("/{userId}/myAlbum")
    @Transactional
    public String save(@RequestParam("file") MultipartFile file, @PathVariable Long userId) throws IOException {
        
        Account user = this.userRepository.getOne(userId);
        
        Picture pi = new Picture();
        pi.setContent(file.getBytes());
        pi.setName(file.getOriginalFilename());
        pi.setMediaType(file.getContentType());
        pi.setSize(file.getSize());
        pi.setUser(user);
        pictureRepository.save(pi);
        
        List<Picture> pictures = new ArrayList<>();
        pictures.add(pi);
        user.setPicAlbum(pictures);
        this.userRepository.save(user);
        Long id = this.pictureRepository.findByUser(user).get(0).getId();

        return "redirect:/" + userId + "/myAlbum";
    }
// show the collection of pics
    @GetMapping("/{userId}/myAlbum")
    public String getAlbum(@PathVariable Long userId, Model model) {
        Account user = this.userRepository.getOne(userId);
        
        model.addAttribute("user", this.userRepository.getOne(userId));
        model.addAttribute("pictures", this.pictureRepository.findByUser(user));
        return "album";
    }
    
// show every pic
    @GetMapping(path = "/{userId}/myAlbum/{id}", produces = "image/*")
    @ResponseBody
    public ResponseEntity<byte[]> viewAlbum(@PathVariable Long id) {
        Picture pi = this.pictureRepository.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pi.getMediaType()));
        headers.setContentLength(pi.getSize());
        headers.add("Content-Disposition", "filnename=" + pi.getName());
        
        return new ResponseEntity<>(pi.getContent(), headers, HttpStatus.CREATED);
        
    }
}
