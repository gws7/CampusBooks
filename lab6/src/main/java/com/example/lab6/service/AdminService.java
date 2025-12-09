package com.example.lab6.service;

import com.example.lab6.model.Admin;
import com.example.lab6.model.BookPost;
import com.example.lab6.model.Student;
import com.example.lab6.repository.AdminRepository;
import com.example.lab6.repository.BookPostRepository;
import com.example.lab6.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BookPostRepository bookPostRepository;

    public Admin login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && admin.getPasswordHash().equals(password)) {
            return admin;
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void blockStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        student.setStatus("BLOCKED");
        studentRepository.save(student);
    }

    public void unblockStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        student.setStatus("ACTIVE");
        studentRepository.save(student);
    }

    public List<BookPost> getAllBookPosts() {
        return bookPostRepository.findAll();
    }

    public void deleteBookPost(Integer postId) {
        bookPostRepository.deleteById(postId);
    }
}

