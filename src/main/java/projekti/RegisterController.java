package projekti;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.service.RegistrationService;

@Controller
@Profile({"production", "default"})
public class RegisterController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RegistrationService registrationService;
    

    @PostMapping("/register")
    @Transactional
    public String register(@ModelAttribute Account account, Model model) {
        
        String result = this.registrationService.newRegistration(account);
        
        if (result.contains("Username")) {
            model.addAttribute("unique", result);
            return "register";
        }
        
        if (result.contains("This string has already existed!")) {
            model.addAttribute("signalUnique", result);
            return "register";
        }
        
        if(result.contains("Length")) {
            model.addAttribute("signalLength", result);
            return "register";
        }

        return "redirect:/login";

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
