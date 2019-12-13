package projekti.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import projekti.Account;
import projekti.AccountRepository;
import projekti.Followership;
import projekti.FollowershipRepository;
import projekti.FollowingMessage;
import projekti.FollowingMessageRepository;
import projekti.Followingship;
import projekti.FollowingshipRepository;
import projekti.Message;

@Service
@Profile({"production", "default", "test"})
public class FollowingService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private FollowershipRepository followershipRepository;
    @Autowired
    private FollowingshipRepository followingshipRepository;
    @Autowired
    private FollowingMessageRepository msgFRepository;
    

    public void following(Long personId) {
        // user is going to follow person
        Account user = this.accountService.getUser();
        Account person = this.userRepository.getOne(personId);

        Followingship following = new Followingship();
        following.setFollowing(personId);
        following.setFamilyname(person.getFamilyname());
        following.setFirstname(person.getFirstname());
        following.setUsername(person.getUsername());
        following.setSignal(person.getSignal());
        following.setTime(LocalDateTime.now());
        following.setUser(user);
        user.getFollowings().add(following);
        this.followingshipRepository.save(following);

        if (!person.getMessages().isEmpty()) {
            for (Message m : person.getMessages()) {
                FollowingMessage msgF = new FollowingMessage();
                msgF.setContent(m.getContent());
                msgF.setTime(m.getTime());
                msgF.setUser(user);
                msgF.setMessageIdentity(m.getId());
                msgF.setWriter(person);
                msgF.setLikes(m.getLikes().size());
                if (m.getPicture() != null) {
                    msgF.setPicture(m.getPicture());
                }

                user.getMsgF().add(msgF);

                this.msgFRepository.save(msgF);
            }
        }

        Followership follower = new Followership();
        follower.setFollower(user.getId());
        follower.setFamilyname(user.getFamilyname());
        follower.setFirstname(user.getFirstname());
        follower.setUsername(user.getUsername());
        follower.setSignal(user.getSignal());
        follower.setTime(LocalDateTime.now());
        follower.setUser(person);
        person.getFollowers().add(follower);

        this.followershipRepository.save(follower);

        this.userRepository.save(user);
        this.userRepository.save(person);
    }

    public void deleteFollowingship(Long followingId) {
        Account user = this.accountService.getUser();
        Account person = this.userRepository.getOne(followingId);

        Followingship f = this.followingshipRepository.findByUserAndFollowing(user, followingId);
        this.followingshipRepository.delete(f);

        Followership follower = this.followershipRepository.findByUserAndFollower(person, user.getId());
        this.followershipRepository.delete(follower);
        
        this.msgFRepository.deleteByUserAndWriter(user, person);
    }

    public void deleteFollowership(Long followerId) {
        Account user = this.accountService.getUser();
        Account person = this.userRepository.getOne(followerId);

        Followership follower = this.followershipRepository.findByUserAndFollower(user, followerId);
        this.followershipRepository.delete(follower);

        Followingship f = this.followingshipRepository.findByUserAndFollowing(person, user.getId());
        this.followingshipRepository.delete(f);
        
        this.msgFRepository.deleteByUserAndWriter(person, user);
    }

}
