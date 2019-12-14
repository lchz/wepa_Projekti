
package projekti.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import projekti.domain.Account;
import projekti.AccountRepository;

@Service 
@Profile({"production", "default", "test"})
public class CustomUserDetailsService implements UserDetailsService{
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account a = this.accountRepository.findByUsername(username);
        if(a == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        
        return new org.springframework.security.core.userdetails.User(
                    a.getUsername(),
                    a.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    Arrays.asList(new SimpleGrantedAuthority("USER")));
        
    }
}
