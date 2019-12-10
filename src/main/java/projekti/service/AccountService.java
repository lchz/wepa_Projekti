package projekti.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projekti.*;

@Service
@Profile({"production", "default"})
public class AccountService {

    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private FollowingMessageRepository msgFRepository;
    
    private Account user;

    public Account getUser() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            user = this.userRepository.findByUsername(username);
        }
        return user;
        
    }
    
    public Account getUser(String signal) {
        
        this.user = this.userRepository.findBySignal(signal);
        return user;
        
    }

    public List<Message> getMessages(String signal) {
        
        user = getUser(signal);
        if(!user.getMessages().isEmpty()) {
            Pageable pageable = PageRequest.of(0, user.getMessages().size(), Sort.by("time").descending());
            return this.messageRepository.findByUser(user, pageable);
        }
        
        return user.getMessages();
        
    }

    public List<Followingship> getFollowings() {
        
        return user.getFollowings();
        
    }

    public List<Followership> getFollowers() {
        
        return user.getFollowers();
        
    }

    public List<FollowingMessage> getFollowingMessages() {
        
        Pageable pageable = PageRequest.of(0, 25, Sort.by("time").descending());
        return this.msgFRepository.findByUser(user, pageable);
        
    }

    public String postNewMessage(Message message) {
        
        if(message.getContent().isEmpty()) {
            return "The field must not be empty!";
        }
        
        Message m = new Message();
        m.setComments(new ArrayList<>());
        m.setContent(message.getContent());
        m.setTime(LocalDateTime.now());
        m.setUser(user);
        this.messageRepository.save(m);

        for (Followership f : user.getFollowers()) {
            FollowingMessage msgF = new FollowingMessage();
            msgF.setContent(m.getContent());
            msgF.setTime(m.getTime());
            msgF.setWriterIdentity(user.getId());
            msgF.setWriterFamilyname(user.getFamilyname());
            msgF.setWriterFirstname(user.getFirstname());
            msgF.setMessageIdentity(m.getId());
            msgF.setLikes(0);
            msgF.setWithPic(false);

            Account person = this.userRepository.getOne(f.getFollower());
            msgF.setUser(person);
            this.msgFRepository.save(msgF);
        }

        user.getMessages().add(m);
        this.userRepository.save(user);
        
        return "";
    }
    

}
