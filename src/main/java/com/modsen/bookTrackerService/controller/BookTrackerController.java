package com.modsen.bookTrackerService.controller;

import com.modsen.bookTrackerService.dto.BookStatusDto;
import com.modsen.bookTrackerService.model.BookStatus;
import com.modsen.bookTrackerService.model.BookStatusEnum;
import com.modsen.bookTrackerService.service.BookTrackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books/tracker")
@RequiredArgsConstructor
public class BookTrackerController {

    private final BookTrackerService bookTrackerService;

    @GetMapping("/{status}")
    public List<BookStatusDto> getBooksByStatus(@PathVariable BookStatusEnum status) {
        
        return bookTrackerService.getBooksByStatus(status);
    }

    @GetMapping("/book-status/{bookId}")
    public ResponseEntity<BookStatusEnum> checkBookStatus(@PathVariable String bookId) {
        BookStatus status = bookTrackerService.getStatus(bookId);
        return ResponseEntity.ok(status.getStatus());
    }

    @PutMapping("/{bookId}/status/{status}")
    public void updateBookStatus(@PathVariable String bookId,
                                 @PathVariable BookStatusEnum status) {
        bookTrackerService.updateBookStatus(bookId, status);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBookStatus(@PathVariable String bookId) {
        bookTrackerService.deleteBookStatus(bookId);
    }
}