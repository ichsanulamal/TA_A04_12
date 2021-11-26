package apap.tugas.siretail.controller;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class PageController {
    @RequestMapping("/")
    private String home() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        System.out.println(authorities);

        return "home";
    }

    @RequestMapping("/login")
    private String login() {
        return "login";
    }
}
