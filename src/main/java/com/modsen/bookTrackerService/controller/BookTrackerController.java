package com.modsen.bookTrackerService.controller;

import com.modsen.bookTrackerService.dto.BookStatusDto;
import com.modsen.bookTrackerService.models.BookStatus;
import com.modsen.bookTrackerService.models.BookStatusEnum;
import com.modsen.bookTrackerService.service.BookTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class BookTrackerController {

    private final BookTrackerService bookTrackerService;

    public BookTrackerController(BookTrackerService bookTrackerService) {
        this.bookTrackerService = bookTrackerService;
    }

    @PostMapping
    public BookStatus createBookStatus(@RequestBody String bookId) {
        return bookTrackerService.createBookStatus(bookId);
    }

    @GetMapping("/{status}")
    public List<BookStatusDto> getBooksByStatus(@PathVariable String status) {
        try {
            BookStatusEnum bookStatusEnum = BookStatusEnum.fromString(status);
            return bookTrackerService.getBooksByStatus(bookStatusEnum);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
    }

    @PutMapping("/{bookId}/status/{status}")
    public void updateBookStatus(@PathVariable String bookId, @PathVariable String status) {
        BookStatusEnum bookStatusEnum;
        try {
            bookStatusEnum = BookStatusEnum.fromString(status);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
        bookTrackerService.updateBookStatus(bookId, String.valueOf(bookStatusEnum));
    }

    @DeleteMapping("/{bookId}")
    public void deleteBookStatus(@PathVariable String bookId) {
        bookTrackerService.deleteBookStatus(bookId);
    }
}