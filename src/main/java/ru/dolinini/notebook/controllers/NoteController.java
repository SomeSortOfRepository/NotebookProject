package ru.dolinini.notebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.dolinini.notebook.model.NotebookEntry;
import ru.dolinini.notebook.model.User;
import ru.dolinini.notebook.repos.NotebookRepo;
import ru.dolinini.notebook.repos.UserRepo;
import ru.dolinini.notebook.security.SecurityUser;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/notebook")
public class NoteController {

    public final UserRepo userRepo;
    public final NotebookRepo notebookRepo;

    @Autowired
    public NoteController(UserRepo userRepo, NotebookRepo notebookRepo) {
        this.userRepo = userRepo;
        this.notebookRepo = notebookRepo;
    }

    @GetMapping("/notes")
    @PreAuthorize("hasAnyAuthority('permission:readnotes', 'permission:read', 'permission:write')")
    public String findAllNotes (Model model) {
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user= userRepo.findByFirstname(userDetails.getUsername()).orElseThrow();
        Iterable<NotebookEntry>list=notebookRepo.findAllByUserId(user.getId());
        model.addAttribute("list", list);
        return "/notebook/main";
    }
//    @GetMapping("/notes/{id}")
//    public String findAllUserNotes (@PathVariable(value="id") Long id, Model model) {
//        Iterable<NotebookEntry>list=notebookRepo.findAllByUserId(id);
//        model.addAttribute("list", list);
//        return "/notebook/main";
//    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('permission:writenotes', 'permission:read', 'permission:write')")
    public String addNote (@ModelAttribute("entry") NotebookEntry entry) {
        return "/notebook/createnote";
    }
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('permission:writenotes', 'permission:read', 'permission:write')")
    public String postNewNote (@ModelAttribute("entry") @Valid NotebookEntry entry, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/notebook/createnote";
        }
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user= userRepo.findByFirstname(userDetails.getUsername()).orElseThrow();
        if(entry.getContent().isEmpty() || entry.getContent().isBlank()) {
            entry.setContent("no content");
        }
        entry.setUser(user);
        entry.setDateOfCreation(new Date());
        notebookRepo.save(entry);
        return "redirect:/notebook/notes";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('permission:writenotes', 'permission:read', 'permission:write')")
    public String editNote (@PathVariable(value="id") Long id, Model model) {
        if(!notebookRepo.existsById(id)) {
            return "redirect:/notebook/notes";
        }
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user= userRepo.findByFirstname(userDetails.getUsername()).orElseThrow();

        if (!user.getId().equals(notebookRepo.findById(id).get().getUser().getId())) {
            return "redirect:/notebook/notes";
        }
        NotebookEntry entry=notebookRepo.findById(id).orElseThrow();
        model.addAttribute("entry", entry);
        return "/notebook/editnote";
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasAnyAuthority('permission:writenotes', 'permission:read', 'permission:write')")
    public String postEditedNote (@ModelAttribute("entry") @Valid NotebookEntry entry, BindingResult bindingResult, @PathVariable(value="id") Long id) {

        if(!notebookRepo.existsById(id)) {
            return "redirect:/notebook/notes";
        }

        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user= userRepo.findByFirstname(userDetails.getUsername()).orElseThrow();

        if (!user.getId().equals(notebookRepo.findById(id).get().getUser().getId())) {
            return "redirect:/notebook/notes";
        }

        if (bindingResult.hasErrors()) {
            return "/notebook/editnote";
        }

        NotebookEntry updatedEntry=notebookRepo.findById(id).orElseThrow();
        updatedEntry.setTitle(entry.getTitle());

        if(entry.getContent().isEmpty() || entry.getContent().isBlank()) {
            updatedEntry.setContent("no content");
        } else {
            updatedEntry.setContent(entry.getContent());
        }
        updatedEntry.setDateOfCreation(new Date());
        notebookRepo.save(updatedEntry);
        return "redirect:/notebook/notes";
    }


    @GetMapping("/{id}/remove")
    @PreAuthorize("hasAnyAuthority('permission:writenotes', 'permission:read', 'permission:write')")
    public String removeNoteById (@PathVariable(value="id") Long id) {

        if(!notebookRepo.existsById(id)) {
            return "redirect:/notebook/notes";
        }

        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user= userRepo.findByFirstname(userDetails.getUsername()).orElseThrow();

        if (!user.getId().equals(notebookRepo.findById(id).get().getUser().getId())) {
            return "redirect:/notebook/notes";
        }

        notebookRepo.deleteById(id);
        return "redirect:/notebook/notes";
    }

}
