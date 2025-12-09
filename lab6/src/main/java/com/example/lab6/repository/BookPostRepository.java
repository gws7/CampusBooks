package com.example.lab6.repository;

import com.example.lab6.model.BookPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookPostRepository extends JpaRepository<BookPost, Integer> {
    List<BookPost> findByOwnerId(Integer ownerId);
    List<BookPost> findByStatus(String status);
}

