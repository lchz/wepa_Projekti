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
public class myWallTest extends org.fluentlenium.adapter.junit.FluentTest {

    @org.springframework.boot.web.server.LocalServerPort
    private Integer port;

    private String uri;
    
    @Before
    public void setUp() {
        this.uri = "http://localhost:" + port;

        goTo(uri);
        find("#username").fill().with("username");
        find("#psw").fill().with("password");
        find("form").first().submit();
    }

    @Test
    public void authUserCanPostNewMsg() throws Throwable {

        assertTrue(window().title().equals("TwitWee Wall"));
        assertFalse(pageSource().contains("Hello TwitWee Hi"));
        find(By.name("content")).fill().with("Hello TwitWee Hi");
        find("#postForm").submit();
        assertTrue(pageSource().contains("Hello TwitWee"));

    }

    @Test
    public void authUserCanLikePosts() throws Throwable {

        assertTrue(window().title().equals("TwitWee Wall"));

        assertTrue(pageSource().contains("0 likes"));

        find("#messageButton").last().click();
        assertTrue(pageSource().contains("1 likes"));

    }

    @Test
    public void authUserCanLikeSamePostOnce() throws Throwable {

        assertTrue(window().title().equals("TwitWee Wall"));
        find(By.name("content")).fill().with("Hello project");
        find("#postForm").submit();

        find("#messageButton").last().click();
        assertTrue(pageSource().contains("1 likes"));

        find("#messageButton").last().click();
        find("#messageButton").last().click();
        assertTrue(pageSource().contains("1 likes"));

    }

    @Test
    public void authUserCanComment() throws Throwable {

        assertTrue(window().title().equals("TwitWee Wall"));
        find(By.name("content")).fill().with("Hello project");
        find("#postForm").submit();

        find("a").last().click();
        assertTrue(window().title().equals("Comment"));

        assertFalse(pageSource().contains("Posted on"));
        assertFalse(pageSource().contains("Hello comment!"));

        find(By.name("comment")).fill().with("Hello comment!");
        find("form").first().submit();

        assertTrue(pageSource().contains("Hello comment!"));
        assertTrue(pageSource().contains("Posted on"));
    }

    @Test
    public void authUserCanGoToMyPosts() throws Throwable {

        assertTrue(window().title().equals("TwitWee Wall"));

        $(withText("My posts")).click();
        assertTrue(window().title().contains("My Posts"));
        assertTrue(pageSource().contains("My posts:"));

    }

    @Test
    public void authUserCanLikeAndCommentOnMyPostsPage() throws Throwable {

        find(By.name("content")).fill().with("Hello project");
        find("#postForm").submit();

        goTo(uri + "/myPosts");
        assertTrue(window().title().equals("My Posts"));

        assertTrue(pageSource().contains("0 likes"));

        find("#likeButton").first().click();
        assertTrue(pageSource().contains("1 likes"));

        find("a").last().click();
        assertTrue(window().title().equals("Comment"));
        assertFalse(pageSource().contains("Good to know!"));
        find(By.name("comment")).fill().with("Good to know!");
        find("form").first().submit();
        assertTrue(pageSource().contains("Good to know!"));

    }

    @Test
    public void authUserCanGoToMyAlbumPage() throws Throwable {

        $(withText("My Album")).click();
        assertTrue(window().title().equals("Album"));
    }

    @Test
    public void authUserCannotPostMsgWithoutPicOnAlbumPage() throws Throwable {

        goTo(uri + "/myAlbum");

        find(By.name("text")).fill().with("A new pic");
        find("#fileButton").first().submit();

        assertFalse(pageSource().contains("A new pic"));
    }

    @Test
    public void authUserCanGoToSearchPage() throws Throwable {
        $(withText("Search")).click();
        assertTrue(window().title().equals("Search"));
    }

    @Test
    public void authUserCanSearchPeople() throws Throwable {
        goTo(uri + "/register");
        find("#username").fill().with("test1");
        find("#firstname").fill().with("first1");
        find("#familyname").fill().with("family1");
        find("#psw").fill().with("test1");
        find("#signal").fill().with("signal1");
        find("form").first().submit();

        goTo(uri);
        find("#username").fill().with("username");
        find("#psw").fill().with("password");
        find("form").first().submit();

        goTo(uri + "/search");
        find(By.name("firstname")).fill().with("first1");
        find("#searchButton").click();
        assertTrue(pageSource().contains("first1 family1"));

        goTo(uri + "/search");
        find(By.name("familyname")).fill().with("family1");
        find("#searchButton").click();
        assertTrue(pageSource().contains("first1 family1"));

        goTo(uri + "/search");
        find(By.name("familyname")).fill().with("qqqq");
        find("#searchButton").click();
        assertTrue(pageSource().contains("No match"));
    }

    @Test
    public void authUserCanFollowPeopleAndCancel() {
        
        goTo(uri + "/register");
        find("#username").fill().with("test1");
        find("#firstname").fill().with("first1");
        find("#familyname").fill().with("family1");
        find("#psw").fill().with("test1");
        find("#signal").fill().with("signal1");
        find("form").first().submit();

        goTo(uri);
        find("#username").fill().with("username");
        find("#psw").fill().with("password");
        find("form").first().submit();
        
        assertTrue(pageSource().contains("Follow 0"));

        goTo(uri + "/search");
        find(By.name("firstname")).fill().with("first1");
        find(By.name("familyname")).fill().with("family1");
        find("#searchButton").click();
        find("#followButton").click();
        assertTrue(window().title().equals("TwitWee Wall"));
        assertTrue(pageSource().contains("Follow 1"));
        
        find("#followingToggle").click();
        assertTrue(pageSource().contains("first1 family1"));
        
        find("#cancelFollowingForm").submit();
        
//        assertTrue(pageSource().contains("first1 family1"));
        // ????????????????????
        assertFalse(pageSource().contains("first1 family1"));
    }
    
    @Test
    public void authUserCanBlockFollowers() throws Throwable {
        goTo(uri + "/register");
        find("#username").fill().with("test1");
        find("#firstname").fill().with("first1");
        find("#familyname").fill().with("family1");
        find("#psw").fill().with("test1");
        find("#signal").fill().with("signal1");
        find("form").first().submit();

        goTo(uri);
        find("#username").fill().with("test1");
        find("#psw").fill().with("test1");
        find("form").first().submit();
        
        goTo(uri + "/search");
        find(By.name("firstname")).fill().with("firstname");
        find("#searchButton").click();
        find("#followButton").click();
        
        goTo(uri + "/login");
        find("#username").fill().with("username");
        find("#psw").fill().with("password");
        find("form").first().submit();
        
        find("#followerToggle").click();
        assertTrue(pageSource().contains("firstname familyname"));
        
//        find("#blockFollowerButton").click();
        find("#blockFollowerForm").submit();
        assertFalse(pageSource().contains("firstname familyname"));
    }

}
