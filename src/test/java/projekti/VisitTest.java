
package projekti;

import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.openqa.selenium.By;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class VisitTest extends org.fluentlenium.adapter.junit.FluentTest {

    @org.springframework.boot.web.server.LocalServerPort
    private Integer port;
    private String uri;

    @Before
    public void setUp() {
        this.uri = "http://localhost:" + port;
    }
    
//    @After
//    public void tearDown() {
//    }

     @Test
     public void authUserCanVisiFollowingANDFollowerPersonsWall() {
        goTo(uri + "/register");
        find("#username").fill().with("test10");
        find("#firstname").fill().with("first10");
        find("#familyname").fill().with("family10");
        find("#psw").fill().with("test10");
        find("#signal").fill().with("signal10");
        find("form").first().submit();

        goTo(uri);
        find("#username").fill().with("test10");
        find("#psw").fill().with("test10");
        find("form").first().submit();

        goTo(uri + "/search");
        find(By.name("firstname")).fill().with("firstname");
        find("#searchButton").click();

        find("#followButton").click();
        
        find("#followingToggle").click();
        find("#visitFollowing").click();
        
        assertTrue(window().title().equals("Visit"));
        assertTrue(pageSource().contains("firstname familyname"));
        assertTrue(pageSource().contains("Logged in as first10 family10"));
        assertFalse(pageSource().contains("#postForm"));
    }
}
