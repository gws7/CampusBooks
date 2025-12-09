package com.example.lab6.service;

import com.example.lab6.model.BookPost;
import com.example.lab6.model.Cart;
import com.example.lab6.model.CartItem;
import com.example.lab6.model.Student;
import com.example.lab6.repository.BookPostRepository;
import com.example.lab6.repository.CartItemRepository;
import com.example.lab6.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private BookPostRepository bookPostRepository;

    public Cart getCartByStudent(Student student) {
        Cart cart = cartRepository.findByStudentId(student.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setStudent(student);
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    @Transactional
    public void addToCart(Student student, Integer bookPostId, Integer quantity) {
        Cart cart = getCartByStudent(student);
        BookPost bookPost = bookPostRepository.findById(bookPostId).orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if item already exists in cart
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getBookPost().getId().equals(bookPostId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setBookPost(bookPost);
            newItem.setQuantity(quantity);
            newItem.setPriceAtAddTime(bookPost.getPrice());
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void removeFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
    
    public List<CartItem> getCartItems(Cart cart) {
        return cartItemRepository.findByCartId(cart.getId());
    }
    
    @Transactional
    public void clearCart(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(items);
    }
}

