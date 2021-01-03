package ru.dolinini.notebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.dolinini.notebook.model.NotebookEntry;
import ru.dolinini.notebook.model.User;
import ru.dolinini.notebook.repos.UserRepo;
import ru.dolinini.notebook.security.Role;
import ru.dolinini.notebook.security.Status;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    public final UserRepo userRepo;

    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/{id}")
    public String findUserById (@PathVariable Long id, Model model) {
        Optional<User> userOptional=userRepo.findById(id);
        User user=userOptional.get();
        return "/user/userpage";
    }

    @GetMapping()
    public String allUsers(Model model) {
        List<User> userList=userRepo.findAll();
        model.addAttribute("users", userList);
        return "/user/usermain";
    }

    @GetMapping("/registration")
    public String addUser(Model model) {
        return "redirect:/users/add";
    }

    @PostMapping("/registration")
    public String addNewUser(@RequestParam String firstname,
                             @RequestParam String lastname,
                             @RequestParam String pass,
                             @RequestParam String email,  Model model) {
        if(userRepo.existsByFirstname(firstname)) {
            String warning="user with such name already exists";
            model.addAttribute("warning", warning);
            return "redirect:/users/add";
        }
        User user=new User(firstname, lastname, pass, email);
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepo.save(user);
        return "/user/createuser";
    }

    @GetMapping("{id}/edit")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        if(!userRepo.existsById(id)) {
            return "redirect:/users";
        }
        User user=userRepo.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "/user/edituser";
    }

    @PostMapping("{id}/edit")
    public String postEditedUser(@PathVariable(value = "id") Long id,
                                 @RequestParam String firstname,
                                 @RequestParam String lastname,
                                 @RequestParam String pass,
                                 @RequestParam String email, Model model) {
        if(!userRepo.existsById(id)) {
            return "redirect:/users/main";
        }
        User user=userRepo.findById(id).orElseThrow();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(pass);
        user.setEmail(email);
        userRepo.save(user);
        return "redirect:/users";
    }

    @GetMapping("{id}/remove")
    public String removeUser(@PathVariable(value = "id") Long id) {
        userRepo.deleteById(id);
        return "redirect:/users";
    }
}
