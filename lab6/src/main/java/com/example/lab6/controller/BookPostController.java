package com.example.lab6.controller;

import com.example.lab6.model.BookPost;
import com.example.lab6.model.Student;
import com.example.lab6.service.BookPostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book-posts")
public class BookPostController {

    @Autowired
    private BookPostService bookPostService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bookPosts", bookPostService.findAll());
        return "book-posts/list";
    }

    @GetMapping("/new")
    public String createForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/students/login";
        }
        model.addAttribute("bookPost", new BookPost());
        return "book-posts/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BookPost bookPost, HttpSession session) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) {
            return "redirect:/students/login";
        }
        bookPost.setOwner(user);
        if (bookPost.getStatus() == null || bookPost.getStatus().isEmpty()) {
            bookPost.setStatus("ACTIVE");
        }
        bookPostService.save(bookPost);
        return "redirect:/book-posts";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        // Simple authorization check
        Student user = (Student) session.getAttribute("user");
        BookPost post = bookPostService.findById(id).orElseThrow();
        if (user == null || !post.getOwner().getId().equals(user.getId())) {
             return "redirect:/book-posts";
        }
        
        model.addAttribute("bookPost", post);
        return "book-posts/form";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        Student user = (Student) session.getAttribute("user");
        BookPost post = bookPostService.findById(id).orElseThrow();
         if (user != null && post.getOwner().getId().equals(user.getId())) {
             bookPostService.deleteById(id);
         }
        return "redirect:/book-posts";
    }
}

