package com.modsen.bookTrackerService.repository;

import com.modsen.bookTrackerService.models.BookStatus;
import com.modsen.bookTrackerService.models.BookStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    List<BookStatus> findByStatus(BookStatusEnum status);
    BookStatus findByBookId(String bookId);
    Optional<BookStatus> findStatusByBookId(String bookId);
}