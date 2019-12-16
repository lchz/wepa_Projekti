
package projekti.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import projekti.CommentRepository;
import projekti.domain.*;
import projekti.MessageRepository;

@Service
@Profile({"production", "default", "test"})
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountService accountService;

    
    @Cacheable("comments")
    public List<Comment> showComments(Long messageId) {
        
        Pageable page = PageRequest.of(0, 10, Sort.by("time").descending());

        return this.commentRepository.findByMessageIdentity(messageId, page);
    }

    @CacheEvict(value = "comments", allEntries = true)
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
        