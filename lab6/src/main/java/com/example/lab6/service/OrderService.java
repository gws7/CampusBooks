package com.example.lab6.service;

import com.example.lab6.model.*;
import com.example.lab6.repository.OrderItemRepository;
import com.example.lab6.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;
    
    @Autowired
    private com.example.lab6.repository.BookPostRepository bookPostRepository;

    @Transactional
    public void checkout(Student buyer) {
        Cart cart = cartService.getCartByStudent(buyer);
        List<CartItem> cartItems = cartService.getCartItems(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Map<Student, List<CartItem>> itemsBySeller = cartItems.stream()
                .collect(Collectors.groupingBy(item -> item.getBookPost().getOwner()));

        for (Map.Entry<Student, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Student seller = entry.getKey();
            List<CartItem> sellerItems = entry.getValue();

            Order order = new Order();
            order.setBuyer(buyer);
            order.setSeller(seller);
            order.setStatus("CREATED");
            
            order = orderRepository.save(order);

            double total = 0;
            for (CartItem cartItem : sellerItems) {
                BookPost bookPost = cartItem.getBookPost();
                
                if (bookPost.getQuantityAvailable() < cartItem.getQuantity()) {
                     throw new RuntimeException("Not enough stock for book: " + bookPost.getTitle());
                }
                
                bookPost.setQuantityAvailable(bookPost.getQuantityAvailable() - cartItem.getQuantity());
                if (bookPost.getQuantityAvailable() == 0) {
                    bookPost.setStatus("SOLD");
                }
                bookPostRepository.save(bookPost);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setBookPost(bookPost);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getPriceAtAddTime());
                orderItemRepository.save(orderItem);

                total += cartItem.getPriceAtAddTime() * cartItem.getQuantity();
            }

            order.setTotalAmount(total);
            orderRepository.save(order);
        }

        cartService.clearCart(cart);
    }

    @Transactional
    public void payOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!"CREATED".equals(order.getStatus())) {
            throw new RuntimeException("Order cannot be paid in current status: " + order.getStatus());
        }
        order.setStatus("PAID");
        orderRepository.save(order);
    }

    @Transactional
    public void shipOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!"PAID".equals(order.getStatus())) {
             throw new RuntimeException("Order cannot be shipped in current status: " + order.getStatus());
        }
        order.setStatus("SHIPPED");
        orderRepository.save(order);
    }

    @Transactional
    public void receiveOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!"SHIPPED".equals(order.getStatus())) {
             throw new RuntimeException("Order cannot be received in current status: " + order.getStatus());
        }
        order.setStatus("COMPLETED");
        orderRepository.save(order);
    }
    
    public List<Order> getOrdersByBuyer(Integer buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }
    
    public List<Order> getOrdersBySeller(Integer sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
