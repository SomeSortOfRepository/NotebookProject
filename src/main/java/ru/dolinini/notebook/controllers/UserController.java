package ru.dolinini.notebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.dolinini.notebook.model.NotebookEntry;
import ru.dolinini.notebook.model.User;
import ru.dolinini.notebook.repos.UserRepo;
import ru.dolinini.notebook.security.Role;
import ru.dolinini.notebook.security.Status;

import javax.validation.Valid;
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

//    @GetMapping("/{id}")
//    public String findUserById (@PathVariable Long id, Model model) {
//        Optional<User> userOptional=userRepo.findById(id);
//        User user=userOptional.get();
//        return "/user/userpage";
//    }

    @GetMapping()
    @PreAuthorize("hasAuthority('permission:read')")
    public String allUsers(Model model) {
        List<User> userList=userRepo.findAll();
        model.addAttribute("users", userList);
        return "/user/usermain";
    }

//    @GetMapping("/registration")
//    public String addUser(Model model) {
//        return "/user/createuser";
//    }
//
//    @PostMapping("/registration")
//    public String addNewUser(@RequestParam String firstname,
//                             @RequestParam String lastname,
//                             @RequestParam String password,
//                             @RequestParam String email,  Model model) {
//        String warning="";
//        if(userRepo.existsByFirstname(firstname)) {
//            warning="ERROR: user with such name already exists, first name must be unique";
//            model.addAttribute("warning", warning);
//            return "redirect:/users/registration";
//        }
//        User user=new User(firstname, lastname, password, email);
//        user.setRole(Role.USER);
//        user.setStatus(Status.ACTIVE);
//        userRepo.save(user);
//        return "redirect:/users";
//    }

    @GetMapping("{id}/edit")
    @PreAuthorize("hasAuthority('permission:write')")
    public String editUser(@PathVariable(value = "id") Long id, Model model) {
        if(!userRepo.existsById(id)) {
            return "redirect:/users";
        }
        User usr=userRepo.findById(id).orElseThrow();
        model.addAttribute("user", usr);
        return "/user/edituser";
    }

    @PostMapping("{id}/edit")
    @PreAuthorize("hasAuthority('permission:write')")
    public String postEditedUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @PathVariable(value = "id") Long id, Model model) {
        if(!userRepo.existsById(id)) {
            return "redirect:/users";
        }
        User updatedUser=userRepo.findById(id).orElseThrow();
        user.setFirstname(updatedUser.getFirstname());
        if (bindingResult.hasFieldErrors("lastname")) {
            return "/user/edituser";
        }
        if (bindingResult.hasFieldErrors("email")) {
            return "/user/edituser";
        }
        updatedUser.setLastname(user.getLastname());
        updatedUser.setEmail(user.getEmail());
        userRepo.save(updatedUser);
        return "redirect:/users";
    }

    @GetMapping("{id}/remove")
    @PreAuthorize("hasAuthority('permission:write')")
    public String removeUser(@PathVariable(value = "id") Long id) {
        userRepo.deleteById(id);
        return "redirect:/users";
    }
}
