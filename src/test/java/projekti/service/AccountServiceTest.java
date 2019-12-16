
package projekti.service;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(username = "username")
public class AccountServiceTest {
    
    @Autowired 
    private AccountService accountService;

    /**
     * Test of getUser method, of class AccountService.
     */
    @Test
    public void testGetUser_0args() {
        assertEquals("username", this.accountService.getUser().getUsername());

    }

    /**
     * Test of getUser method, of class AccountService.
     */
    @Test
    public void testGetUser_String() {
        assertEquals("familyname", this.accountService.getUser("signal").getFamilyname());
    }
    
}
