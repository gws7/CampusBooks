package com.example.lab6.service;

import com.example.lab6.model.BookPost;
import com.example.lab6.repository.BookPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookPostService {

    @Autowired
    private BookPostRepository bookPostRepository;

    public List<BookPost> findAll() {
        return bookPostRepository.findAll();
    }

    public Optional<BookPost> findById(Integer id) {
        return bookPostRepository.findById(id);
    }

    public BookPost save(BookPost bookPost) {
        return bookPostRepository.save(bookPost);
    }

    public void deleteById(Integer id) {
        bookPostRepository.deleteById(id);
    }

    public List<BookPost> findByOwner(Integer ownerId) {
        return bookPostRepository.findByOwnerId(ownerId);
    }
}

