
package projekti.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projekti.*;

@Service
public class AlbumService {
    
    @Autowired
    private AccountService accountService;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private FollowingMessageRepository msgFRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ThumbUpRepository thumbUpRepository;
    
    
    // add m and p to the followingMessage
    public String saveNewPic(MultipartFile file, String text) throws IOException {
        
        Account user = this.accountService.getUser();
        
        if(this.pictureRepository.findByUser(user).size() == 10) {
            return "Your album is full. Delete some pictures first.";
        }
            
        Picture pi = new Picture();
        pi.setContent(file.getBytes());
        pi.setName(file.getOriginalFilename());
        pi.setMediaType(file.getContentType());
        pi.setSize(file.getSize());
        pi.setUser(user);

        Message m = new Message();
        m.setContent(text);
        m.setTime(LocalDateTime.now());
        m.setUser(user);
        m.setPicture(pi);

        pi.setMessage(m);

        this.pictureRepository.save(pi);
        this.messageRepository.save(m);

        user.getPicAlbum().add(pi);
        this.userRepository.save(user);

        List<FollowingMessage> userMsgF = new ArrayList<>();
        for (Followership f : user.getFollowers()) {
            FollowingMessage msgF = new FollowingMessage();
            msgF.setContent(m.getContent());
            msgF.setTime(m.getTime());
            msgF.setWriterIdentity(user.getId());
            msgF.setWriterFamilyname(user.getFamilyname());
            msgF.setWriterFirstname(user.getFirstname());
            msgF.setMessageIdentity(m.getId());
            msgF.setLikes(0);
            msgF.setWithPic(true);

            Account person = this.userRepository.getOne(f.getFollower());
            msgF.setUser(person);
            this.msgFRepository.save(msgF);
        }
        return "";
        
    }

    // show the collection of pics and messages
    public List<Picture> getPictures(Account user) {
        
        return this.pictureRepository.findByUser(user);
    
    }

    public void deletePic(Long picId) {
        
        Account user = this.accountService.getUser();

        Picture pic = this.pictureRepository.getOne(picId);
        Message m = pic.getMessage();
        Long messageId = m.getId();

        this.thumbUpRepository.deleteByMessage(m);
        this.commentRepository.deleteByMessageIdentity(messageId);
        this.messageRepository.deleteById(messageId);
        this.msgFRepository.deleteByMessageIdentity(messageId);
        
        user.getPicAlbum().remove(pic);
        user.getMessages().remove(m);
        user.getComments().removeAll(m.getComments());
        this.userRepository.save(user);
        
        this.pictureRepository.deleteById(picId);
    }

    // show every pic
    public ResponseEntity<byte[]> viewPic(Long id) {
        
        Picture pi = this.pictureRepository.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pi.getMediaType()));
        headers.setContentLength(pi.getSize());
        headers.add("Content-Disposition", "filnename=" + pi.getName());

        return new ResponseEntity<>(pi.getContent(), headers, HttpStatus.CREATED);

    }

    // add comment to a pic
    public void addComment(Long messageId, String comment) {
        
        Account user = this.accountService.getUser();

        Comment c = new Comment();
        c.setContent(comment);
        c.setMessageIdentity(messageId);
        c.setTime(LocalDateTime.now());
        c.setWriter(user);

        this.commentRepository.save(c);
        this.messageRepository.getOne(messageId).getComments().add(c);
        
    }

    // show comments of the pic
    public List<Comment> showComments(Long messageId) {
        
        Pageable page = PageRequest.of(0, 10, Sort.by("time").descending());
        return this.commentRepository.findByMessageIdentity(messageId, page);
        
    }
    
    // set profile pic
    public void setProfilePic(Long picId) {
        
        Account user = this.accountService.getUser();
        user.setProfilePic(this.pictureRepository.getOne(picId));
        
    }
    
}
