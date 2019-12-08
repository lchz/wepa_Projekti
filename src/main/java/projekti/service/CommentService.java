
package projekti.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import projekti.Account;
import projekti.Comment;
import projekti.CommentRepository;
import projekti.Message;
import projekti.MessageRepository;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountService accountService;

    
    public List<Comment> showComments(Long messageId) {
        
        Pageable page = PageRequest.of(0, 10, Sort.by("time").descending());

        return this.commentRepository.findByMessageIdentity(messageId, page);
    }

    public void postComment(String comment, Long messageId) {
        
        Account user = this.accountService.getUser();
        Comment c = new Comment();

        c.setMessageIdentity(messageId);
        c.setContent(comment);
        c.setTime(LocalDateTime.now());
        c.setWriter(user); // comment writer

        Message m = this.messageRepository.getOne(messageId);
        m.getComments().add(c);

        this.commentRepository.save(c);
        this.messageRepository.save(m);
    }
    
}
