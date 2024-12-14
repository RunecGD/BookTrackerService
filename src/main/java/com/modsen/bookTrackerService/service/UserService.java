package com.modsen.bookTrackerService.service;

import com.modsen.bookTrackerService.dto.UserDto;
import com.modsen.bookTrackerService.models.UserTracker;
import com.modsen.bookTrackerService.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserTracker register(UserDto UserDto) {
        UserTracker user = new UserTracker();
        user.setUsername(UserDto.username());
        user.setPassword(passwordEncoder.encode(UserDto.password()));
        return userRepository.save(user);
    }

    public UserTracker findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
