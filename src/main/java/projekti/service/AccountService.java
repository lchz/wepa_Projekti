package projekti.service;

import projekti.domain.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projekti.*;

@Service
@Profile({"production", "default", "test"})
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

    @Cacheable("messages")
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

    @CacheEvict(value = "messages", allEntries = true)
    public String postNewMessage(Message message) {
        
        if(message.getContent().isEmpty()) {
            return "The field must not be empty!";
        }
        
        user = this.getUser();
        
        message.setComments(new ArrayList<>());
        message.setTime(LocalDateTime.now());
        message.setUser(user);
        this.messageRepository.save(message);
        
        FollowingMessage myMessage = new FollowingMessage();
        myMessage.setContent(message.getContent());
        myMessage.setTime(message.getTime());
        myMessage.setMessageIdentity(message.getId());
        myMessage.setLikes(0);
        myMessage.setUser(user);
        myMessage.setWriter(user);
        this.msgFRepository.save(myMessage);

        for (Followership f : user.getFollowers()) {
            FollowingMessage msgF = new FollowingMessage();
            msgF.setContent(message.getContent());
            msgF.setTime(message.getTime());
            msgF.setMessageIdentity(message.getId());
            msgF.setWriter(user);
            msgF.setLikes(0);

            Account person = this.userRepository.getOne(f.getFollower());
            msgF.setUser(person);
            
            this.msgFRepository.save(msgF);
        }

        user.getMessages().add(message);
        this.userRepository.save(user);
        
        return "";
    }
    

}
