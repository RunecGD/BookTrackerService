package com.modsen.book_tracker_service.repository;

import com.modsen.book_tracker_service.models.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
    List<BookStatus> findByStatus(String status);
    BookStatus findByBookId(String bookId);
}