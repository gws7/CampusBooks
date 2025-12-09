package com.example.lab6;

import com.example.lab6.model.Admin;
import com.example.lab6.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(AdminRepository adminRepository) {
        return args -> {
            if (adminRepository.findByEmail("admin@campusbooks.com") == null) {
                Admin admin = new Admin();
                admin.setEmail("admin@campusbooks.com");
                admin.setPasswordHash("admin123"); 
                adminRepository.save(admin);
                System.out.println("Default admin created: admin@campusbooks.com / admin123");
            }
        };
    }
}

