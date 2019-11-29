
package projekti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if(this.accountRepository.findByUsername(username) != null) {
            return "redirect:/*";
        }
        
        Account a = new Account();
        a.setUsername(username);
        a.setPassword(this.passwordEncoder.encode(password));
        this.accountRepository.save(a);
        return "redirect:/register";
        
    }
    
    @GetMapping("/register") 
    public String register(Model model) {
        model.addAttribute("accounts", this.accountRepository.findAll());
        return "register";
    }

    
    @GetMapping("/login")
    public String list(Model model) {
//        model.addAttribute("accounts", accountRepository.findAll());
        return "login";
    }
}
