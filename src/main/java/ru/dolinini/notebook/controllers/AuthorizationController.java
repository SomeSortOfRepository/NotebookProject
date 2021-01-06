package ru.dolinini.notebook.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dolinini.notebook.model.User;
import ru.dolinini.notebook.repos.UserRepo;
import ru.dolinini.notebook.security.Role;
import ru.dolinini.notebook.security.Status;

@Controller
@RequestMapping("/auth")
public class AuthorizationController {

    public final UserRepo userRepo;


    public AuthorizationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/auth/login";
    }
    @GetMapping("/success")
    public String getSuccessAuthorizationPage() {
        return "/auth/success";
    }

    @GetMapping("/registration")
    public String addUser(Model model) {
        return "/user/createuser";
    }

    @PostMapping("/registration")
    public String addNewUser(@RequestParam String firstname,
                             @RequestParam String lastname,
                             @RequestParam String password,
                             @RequestParam String email,  Model model) {

        if(userRepo.existsByFirstname(firstname)) {
            String warning="error";
            model.addAttribute("warning", warning);
            return "/user/createuser";
        }
        String pass=new BCryptPasswordEncoder(12).encode(password);
        User user=new User(firstname, lastname, pass, email);
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepo.save(user);
        String warning="success";
        model.addAttribute("warning", warning);
        return "/auth/login";
    }
}
