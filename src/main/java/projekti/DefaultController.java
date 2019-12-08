package projekti;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile({"production", "default"})
public class DefaultController {

    @GetMapping("/")
    public String helloWorld() {
        return "redirect:/login";
    }

}
