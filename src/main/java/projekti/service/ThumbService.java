package projekti.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import projekti.Account;
import projekti.FollowingMessage;
import projekti.FollowingMessageRepository;
import projekti.Message;
import projekti.MessageRepository;
import projekti.ThumbUp;
import projekti.ThumbUpRepository;

@Service
@Profile({"production", "default"})
public class ThumbService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private FollowingMessageRepository msgFRepository;
    @Autowired
    private ThumbUpRepository likeRepository;

    
    public void addLike(Long messageId) {
        Account user = this.accountService.getUser();
        Message m = this.messageRepository.getOne(messageId);

        if (this.likeRepository.existsByUserAndMessage(user, m)) {
            return;
        }

        ThumbUp like = new ThumbUp();
        like.setUser(user);
        like.setMessage(m);

        int likes = m.getLikes().size();
        m.getLikes().add(like);
        
        List<FollowingMessage> fms = this.msgFRepository.findByMessageIdentity(messageId);
        if (!fms.isEmpty()) {
            for(FollowingMessage fm: fms) {
                fm.setLikes(likes + 1);
                this.msgFRepository.save(fm);
            }
        }
        
        this.messageRepository.save(m);
        this.likeRepository.save(like);
        
    }

}
