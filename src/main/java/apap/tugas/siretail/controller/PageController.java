package apap.tugas.siretail.controller;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class PageController {

    public static Integer msg = 0;

    @RequestMapping("/")
    private String home() {
        //izin nambahin buat code gw
        PageController.msg = 0;

        return "home";
    }

    @RequestMapping("/login")
    private String login() {
        return "login";
    }
}
