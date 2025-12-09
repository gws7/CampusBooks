package com.example.lab6.controller;

import com.example.lab6.model.Cart;
import com.example.lab6.model.Student;
import com.example.lab6.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) {
            return "redirect:/students/login";
        }
        Cart cart = cartService.getCartByStudent(user);
        model.addAttribute("cartItems", cartService.getCartItems(cart));
        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Integer bookPostId, @RequestParam(defaultValue = "1") Integer quantity, HttpSession session) {
        Student user = (Student) session.getAttribute("user");
        if (user == null) {
            return "redirect:/students/login";
        }
        cartService.addToCart(user, bookPostId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Integer id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }
}

