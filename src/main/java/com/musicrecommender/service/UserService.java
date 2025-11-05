package com.musicrecommender.service;

import com.musicrecommender.model.User;
import com.musicrecommender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register user
    public User registerUser(String email, String password, String name) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(name); 
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    // ✅ Validate user for login
    public User validateUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // ✅ Find by email if needed elsewhere
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
