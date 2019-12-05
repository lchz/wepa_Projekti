
package projekti;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @RequestParam(required=false) String username, @RequestParam(required=false) String password, 
//                            @RequestParam(required=false) String firstname, @RequestParam(required=false) String familyname
    @PostMapping("/register")
    public String register(@ModelAttribute Account a) {
        
        if(this.accountRepository.findByUsername(a.getUsername()) != null) {
            return "redirect:/*";
        }
        
        if(a.getUsername()!= null && !a.getUsername().isEmpty() && a.getPassword() != null && !a.getPassword().isEmpty()) {
            a.setPassword(this.passwordEncoder.encode(a.getPassword()));
            this.accountRepository.save(a);
        }
        
        return "redirect:/register";
        
    }
    
    @GetMapping("/register") 
    public String register(Model model) {
        model.addAttribute("accounts", this.accountRepository.findAll());
        return "register";
    }

    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
