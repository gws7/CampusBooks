package com.example.lab6.controller;

import com.example.lab6.model.Student;
import com.example.lab6.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) {
            return "redirect:/students/login";
        }
        orderService.createOrder(user);
        return "redirect:/orders/history";
    }

    @GetMapping("/history")
    public String orderHistory(HttpSession session, Model model) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) {
            return "redirect:/students/login";
        }
        model.addAttribute("orders", orderService.getOrdersByBuyer(user.getId()));
        return "orders/history";
    }
}

