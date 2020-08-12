package cc.mrbird.febs.auth.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * @author MrBird
 */
@Controller
public class SecurityController {

    @ResponseBody
    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

}
