package com.task_management_mini_system.task_management.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.task_management_mini_system.task_management.model.User;
import com.task_management_mini_system.task_management.model.enums.Role;
import com.task_management_mini_system.task_management.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found:"+ username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                java.util.List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }


    public User registerUser(String username, String email, String rawPassword) {

        if (repository.existsByUsername(username)) {
            throw new RuntimeException("Username  have been token");
        }

        if (repository.existsByEmail(email)) {
            throw new RuntimeException("Email already used");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword)); 
        user.setRole(Role.ROLE_USER); 

        return repository.save(user);
    }

//---------------------------CRUD------------------------------ 

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    public User createUser(User user) {

    	user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User updateUser(Long id, User userUpdated) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userUpdated.getUsername());
        user.setEmail(userUpdated.getEmail());
        user.setRole(userUpdated.getRole());

        if (userUpdated.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdated.getPassword()));
        }

        return repository.save(user);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
