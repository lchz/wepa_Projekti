package projekti.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import projekti.Account;
import projekti.AccountRepository;
import projekti.Followingship;

@Service
@Profile({"production", "default", "test"})
public class SearchService {

    @Autowired
    private AccountRepository userRepository;
    @Autowired
    private AccountService accountService;
    private List<Account> users;

    public List<Account> foundUsers(String firstname, String familyname) {
        this.users = new ArrayList<>();

        if (firstname.isEmpty() && !familyname.isEmpty()) {
            users = this.userRepository.findByFamilyname(familyname);
        } else if (familyname.isEmpty() && !firstname.isEmpty()) {
            users = this.userRepository.findByFirstname(firstname);
        } else if (!familyname.isEmpty() && !firstname.isEmpty()) {
            users = this.userRepository.findByFirstnameAndFamilyname(firstname, familyname);
        }

        return users;
    }

    public List<Account> foundFollowing() {
        Account user = this.accountService.getUser();

        List<Followingship> followed = user.getFollowings();
        List<Account> followedToDisplay = new ArrayList<>();

        for (int i = 0; i < followed.size(); i++) {
            Account followingPerson = this.userRepository.getOne(followed.get(i).getFollowing());
            if (users.contains(followingPerson)) {
                users.remove(followingPerson);
                followedToDisplay.add(followingPerson);
            }
        }
        
        return followedToDisplay;
    }

}
