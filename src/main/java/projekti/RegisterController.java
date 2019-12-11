package projekti;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    

    @ModelAttribute
    private Account getAccount() {
        return new Account();
    }

    @PostMapping("/register")
    @Transactional
    public String register(@Valid @ModelAttribute Account account, BindingResult bindingResult, Model model) {
        
        String result = this.registrationService.newRegistration(account);
        
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        if (result.contains("Username")) {
            model.addAttribute("unique", result);
            return "register";
        }
        
        if (result.contains("string")) {
            model.addAttribute("signalUnique", result);
            return "register";
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
