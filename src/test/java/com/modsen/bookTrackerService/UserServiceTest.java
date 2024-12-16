package com.modsen.bookTrackerService;

import com.modsen.bookTrackerService.dto.UserDto;
import com.modsen.bookTrackerService.models.UserTracker;
import com.modsen.bookTrackerService.repository.UserRepository;
import com.modsen.bookTrackerService.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        
        UserDto userDto = new UserDto("testuser", "password");
        UserTracker user = new UserTracker();
        user.setUsername(userDto.username());

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(userDto.password())).thenReturn(encodedPassword);

        when(userRepository.save(any(UserTracker.class))).thenAnswer(invocation -> {
            UserTracker savedUser = invocation.getArgument(0);
            savedUser.setPassword(encodedPassword); 
            return savedUser;
        });

        
        UserTracker registeredUser = userService.register(userDto);

        
        assertNotNull(registeredUser);
        assertEquals(userDto.username(), registeredUser.getUsername());
        assertEquals(encodedPassword, registeredUser.getPassword());
        verify(userRepository).save(any(UserTracker.class));
    }

    @Test
    public void testFindByUsername_UserFound() {
        
        String username = "testuser";
        UserTracker user = new UserTracker();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        
        UserTracker foundUser = userService.findByUsername(username);

        
        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
    }

    @Test
    public void testFindByUsername_UserNotFound() {
        
        String username = "unknownuser";

        when(userRepository.findByUsername(username)).thenReturn(null);

        
        UserTracker foundUser = userService.findByUsername(username);

        
        assertNull(foundUser);
    }
}
