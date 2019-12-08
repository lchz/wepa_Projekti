//
//package projekti.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.Model;
//import projekti.Account;
//import projekti.AccountRepository;
//
//@Service
//public class RegistrationService {
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    @Cacheable()
//    public void newRegistration(Model model, Account account) {
//        
//        if (this.accountRepository.findByUsername(account.getUsername()) != null) {
//            model.addAttribute("unique", "Username has already existed!");
//            return "register";
//        }
//
//        if (bindingResult.hasErrors()) {
//            return "register";
//        }
//
//        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
//
////            a.setProfilePic(defaultProfilePic);
//        this.accountRepository.save(account);
//        
//    }
//    
//}
