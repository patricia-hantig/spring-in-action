package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller                         // identifies the class as a component for component scanning - Spring automatically creates an instance of HomeController as a bean in the Spring application context
public class HomeController {

    @GetMapping("/")                // indicates that if a HTTP GET request is received for '/' path -> then this method will handle the request
    public String home() {
        return "home";
    }
}
