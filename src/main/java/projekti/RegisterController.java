package projekti;

import java.util.ArrayList;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @ModelAttribute
    private Account getAccount() {
        return new Account();
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Account account, BindingResult bindingResult, Model model) {

        if (this.accountRepository.findByUsername(account.getUsername()) != null) {
            model.addAttribute("unique", "Username has already existed!");
            return "register";
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        account.setPassword(this.passwordEncoder.encode(account.getPassword()));

//            a.setProfilePic(defaultProfilePic);
        this.accountRepository.save(account);

        return "redirect:/register";

    }

    @GetMapping("/register")
    @Cacheable("registration")
    public String register(Model model) {
        model.addAttribute("accounts", this.accountRepository.findAll());
        return "register";
    }

    @GetMapping("/login")
    public String login(Model model) {

        return "login";
    }
}
