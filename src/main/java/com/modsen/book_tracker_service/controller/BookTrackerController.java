package com.modsen.book_tracker_service.controller;
import com.modsen.book_tracker_service.models.BookStatus;
import com.modsen.book_tracker_service.service.BookTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookTrackerController {

    @Autowired
    private BookTrackerService bookTrackerService;

    @PostMapping
    public BookStatus createBookStatus(@RequestBody String bookId) {
        return bookTrackerService.createBookStatus(bookId);
    }

    @GetMapping("/available")
    public List<BookStatus> getAvailableBooks() {
        return bookTrackerService.getAvailableBooks();
    }

    @PutMapping("/{bookId}/status/{status}")
    public void updateBookStatus(@PathVariable String bookId, @PathVariable String status) {
        bookTrackerService.updateBookStatus(bookId, status);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBookStatus(@PathVariable String bookId) {
        bookTrackerService.deleteBookStatus(bookId);
    }
}

