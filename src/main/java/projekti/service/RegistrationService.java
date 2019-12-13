
package projekti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekti.Account;
import projekti.AccountRepository;

@Service
@Profile({"production", "default", "test"})
public class RegistrationService {
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    public String newRegistration(Account account) {
        
        if (this.accountRepository.findByUsername(account.getUsername()) != null) {
            return "Username has already existed!";
        }
        
        if (this.accountRepository.findBySignal(account.getSignal()) != null) {
            return "This string has already existed!";
        }
        
        if(account.getSignal().length() < 4 || account.getSignal().length() > 20) {
            return "String must be 4-20 characters long!";
        }

        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        this.accountRepository.save(account);
        
        return "";
        
    }
    
}
