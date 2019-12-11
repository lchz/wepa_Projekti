
package projekti.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekti.Account;
import projekti.AccountRepository;
import projekti.Followership;
import projekti.FollowershipRepository;

@Service
@Profile({"production", "default"})
public class RegistrationService {
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FollowershipRepository followershipRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    public String newRegistration(Account account) {
        
//        if(account == null) {
//            return
//        }
        
        if (this.accountRepository.findByUsername(account.getUsername()) != null) {
            return "Username has already existed!";
        }
        
        if (this.accountRepository.findBySignal(account.getSignal()) != null) {
            return "This string has already existed!";
        }

        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        this.accountRepository.save(account);
        
        return "";
        
    }
    
}
