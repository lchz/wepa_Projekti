package projekti;

import projekti.domain.Account;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile({"production", "default", "test"})
public class DefaultController {
    
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String helloWorld() {
        return "redirect:/login";
    }
    
    @PostConstruct
    public void init() {
        if(this.accountRepository.findByUsername("username") == null) {
            Account user = new Account();
            user.setUsername("username");
            user.setPassword(this.passwordEncoder.encode("password"));
            user.setFamilyname("familyname");
            user.setFirstname("firstname");
            user.setSignal("signal");
            this.accountRepository.save(user);
        }
    }

}
