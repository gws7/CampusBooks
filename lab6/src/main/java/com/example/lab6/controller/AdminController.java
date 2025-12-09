package com.example.lab6.controller;

import com.example.lab6.model.Admin;
import com.example.lab6.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Admin admin = adminService.login(email, password);
        if (admin != null) {
            session.setAttribute("admin", admin);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid admin credentials");
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("students", adminService.getAllStudents());
        model.addAttribute("posts", adminService.getAllBookPosts());
        return "admin/dashboard";
    }

    @PostMapping("/student/block/{id}")
    public String blockStudent(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        adminService.blockStudent(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/student/unblock/{id}")
    public String unblockStudent(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        adminService.unblockStudent(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        adminService.deleteBookPost(id);
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}

