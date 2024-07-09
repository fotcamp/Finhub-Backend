package fotcamp.finhub.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/home")
    public String home() {
        return "home"; // home.jsp로 포워딩
    }
}
