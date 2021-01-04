package ru.dolinini.notebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.dolinini.notebook.model.NotebookEntry;
import ru.dolinini.notebook.model.User;
import ru.dolinini.notebook.repos.NotebookRepo;
import ru.dolinini.notebook.repos.UserRepo;

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
    public String findAllNotes (Model model) {
        Iterable<NotebookEntry>list=notebookRepo.findAll();
        model.addAttribute("list", list);
        return "/notebook/main";
    }
    @GetMapping("/notes/{id}")
    public String findAllUserNotes (@PathVariable(value="id") Long id, Model model) {
        Iterable<NotebookEntry>list=notebookRepo.findAllByUserId(id);
        model.addAttribute("list", list);
        return "/notebook/main";
    }

    @GetMapping("/add")
    public String addNote () {
        return "/notebook/createnote";
    }
    @PostMapping("/add")
    public String postNewNote (@RequestParam String title, @RequestParam String content, Model model) {
        NotebookEntry newEntry=new NotebookEntry(title, content);
        notebookRepo.save(newEntry);
        return "redirect:/notebook/notes";
    }

    @GetMapping("/{id}/edit")
    public String editNote (@PathVariable(value="id") Long id, Model model) {
        if(!notebookRepo.existsById(id)) {
            return "redirect:/notebook/main";
        }
        NotebookEntry entry=notebookRepo.findById(id).orElseThrow();
        model.addAttribute("entry", entry);
        return "/notebook/editnote";
    }

    @PostMapping("/{id}/edit")
    public String postEditedNote (@RequestParam String title, @RequestParam String content, @PathVariable(value="id") Long id, Model model) {
        if(!notebookRepo.existsById(id)) {
            return "redirect:/notebook/main";
        }
        NotebookEntry entry=notebookRepo.findById(id).orElseThrow();
        entry.setTitle(title);
        entry.setContent(content);
        entry.setDateOfCreation(new Date());
        notebookRepo.save(entry);
        return "redirect:/notebook/notes";
    }


    @GetMapping("/{id}/remove")
    public String removeNoteById (@PathVariable(value="id") Long id) {
        notebookRepo.deleteById(id);
        return "redirect:/notebook/notes";
    }

}
