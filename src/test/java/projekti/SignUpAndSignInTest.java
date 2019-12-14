package projekti;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withText;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class SignUpAndSignInTest extends org.fluentlenium.adapter.junit.FluentTest {

    @org.springframework.boot.web.server.LocalServerPort
    private Integer port;
    private String uri;

    @Before
    public void setUp() {
        this.uri = "http://localhost:" + port;
    }

    @Test
    public void pageShouldNotBeDirectlyAccessible() throws Throwable {
        goTo(uri + "/myWall");
        assertTrue(pageSource().contains("Sign in"));
    }

    @Test
    public void shouldSeeLoginPage() throws Throwable {
        goTo(uri + "/login");
        assertThat(find(By.name("username"))).isNotNull();
        assertThat(find(By.name("password"))).isNotNull();
    }

    @Test
    public void userCanGoFromInToUp() throws Throwable {
        goTo(uri + "/login");
        find("a").last().click();
        assertTrue(window().title().equals("Register"));
    }

    @Test
    public void userCanSignUpThenSignIn() throws Throwable {
        goTo(uri + "/register");
        find("#username").fill().with("test3");
        find("#firstname").fill().with("first3");
        find("#familyname").fill().with("family3");
        find("#psw").fill().with("test3");
        find("#signal").fill().with("signal3");
        find("form").first().submit();
        assertTrue(pageSource().contains("Sign in"));

        find("#username").fill().with("test3");
        find("#psw").fill().with("test3");
        find("form").first().submit();
        assertTrue(window().title().equals("TwitWee Wall"));

    }

    @Test
    public void userCannotSignInWithWrongDetails() throws Throwable {
        goTo(uri + "/login");
        find("#username").fill().with("username");
        find("#psw").fill().with("sssssssssss");
        find("form").first().submit();
        assertTrue(pageSource().contains("Sign in"));
        assertFalse(pageSource().contains("About me"));
    }
    
    @Test
    public void userCanLogOut() throws Throwable {
        goTo(uri + "/login");
        find("#username").fill().with("username");
        find("#psw").fill().with("password");
        find("form").first().submit();
        assertTrue(window().title().equals("TwitWee Wall"));
        
        $(withText("Log out")).click();
        assertTrue(pageSource().contains("Sign in"));
    }

}
