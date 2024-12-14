package com.modsen.bookTrackerService.repository;

import com.modsen.bookTrackerService.models.UserTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserTracker, Long> {
    UserTracker findByUsername(String username);
}
