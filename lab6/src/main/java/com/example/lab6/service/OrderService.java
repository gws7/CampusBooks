package com.example.lab6.service;

import com.example.lab6.model.*;
import com.example.lab6.repository.OrderItemRepository;
import com.example.lab6.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrder(Student buyer) {
        Cart cart = cartService.getCartByStudent(buyer);
        List<CartItem> cartItems = cartService.getCartItems(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create Order
        Order order = new Order();
        order.setBuyer(buyer);
        order.setStatus("PENDING");
        // Assume single seller for simplicity or handle multiple sellers logic here.
        // For now, let's take the seller from the first item (simplified)
        if (!cartItems.isEmpty()) {
            order.setSeller(cartItems.get(0).getBookPost().getOwner());
        }
        
        double total = 0;
        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBookPost(cartItem.getBookPost());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPriceAtAddTime());
            orderItemRepository.save(orderItem);

            total += cartItem.getPriceAtAddTime() * cartItem.getQuantity();
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        // Clear cart
        cartService.clearCart(cart);

        return order;
    }
    
    public List<Order> getOrdersByBuyer(Integer buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }
    
    public List<Order> getOrdersBySeller(Integer sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }
}

