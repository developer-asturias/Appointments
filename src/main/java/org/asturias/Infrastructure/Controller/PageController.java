package org.asturias.Infrastructure.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    // esto lo usamos para la redirigir a los templates de cada pagina
//    @GetMapping("/sign-in")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/sessions")
//    public String createSessions() {
//        return "create-sessions";
//    }
//
//    @GetMapping("/get-sessions")
//    public String getSessions() {
//        return "sessions";
//    }

    @GetMapping("/appointment")
    public String createAppointment() {
        return "appointment";
    }

    @GetMapping("/admin")
    public String pageIndex() {
        return "admin";
    }

}
