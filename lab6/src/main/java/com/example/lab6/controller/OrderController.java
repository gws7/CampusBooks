package com.example.lab6.controller;

import com.example.lab6.model.Order;
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
        orderService.checkout(user);
        return "redirect:/orders/buying";
    }

    @GetMapping("/buying")
    public String buyingHistory(HttpSession session, Model model) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) {
            return "redirect:/students/login";
        }
        model.addAttribute("orders", orderService.getOrdersByBuyer(user.getId()));
        return "orders/buying";
    }

    @GetMapping("/selling")
    public String sellingHistory(HttpSession session, Model model) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) {
            return "redirect:/students/login";
        }
        model.addAttribute("orders", orderService.getOrdersBySeller(user.getId()));
        return "orders/selling";
    }

    @PostMapping("/pay/{id}")
    public String payOrder(@PathVariable Integer id, HttpSession session) {
        Student user = (Student) session.getAttribute("user");
         if (user == null) return "redirect:/students/login";
         
        Order order = orderService.getOrderById(id);
        if (!order.getBuyer().getId().equals(user.getId())) {
             return "redirect:/orders/buying";
        }

        orderService.payOrder(id);
        return "redirect:/orders/buying";
    }

    @PostMapping("/ship/{id}")
    public String shipOrder(@PathVariable Integer id, HttpSession session) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) return "redirect:/students/login";

        Order order = orderService.getOrderById(id);
        if (!order.getSeller().getId().equals(user.getId())) {
             return "redirect:/orders/selling";
        }

        orderService.shipOrder(id);
        return "redirect:/orders/selling";
    }

    @PostMapping("/receive/{id}")
    public String receiveOrder(@PathVariable Integer id, HttpSession session) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) return "redirect:/students/login";

        Order order = orderService.getOrderById(id);
        if (!order.getBuyer().getId().equals(user.getId())) {
             return "redirect:/orders/buying";
        }

        orderService.receiveOrder(id);
        return "redirect:/orders/buying";
    }
}
