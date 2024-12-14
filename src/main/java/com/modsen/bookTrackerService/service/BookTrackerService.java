package com.modsen.bookTrackerService.service;

import com.modsen.bookTrackerService.dto.BookStatusDto;
import com.modsen.bookTrackerService.models.BookStatus;
import com.modsen.bookTrackerService.models.BookStatusEnum;
import com.modsen.bookTrackerService.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookTrackerService {

    @Autowired
    private BookStatusRepository bookStatusRepository;

    public BookStatus createBookStatus(String bookId) {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);
        bookStatus.setStatus(BookStatusEnum.AVAILABLE);

        return bookStatusRepository.save(bookStatus);
    }

    public List<BookStatusDto> getBooksByStatus(BookStatusEnum status) {
        return bookStatusRepository.findByStatus(status).stream()
                .map(bookStatus -> new BookStatusDto(
                        bookStatus.getBookId(),
                        bookStatus.getStatus(),
                        bookStatus.getBorrowedAt(),
                        bookStatus.getReturnBy()
                ))
                .collect(Collectors.toList()
                );
    }

    public void updateBookStatus(String bookId, String status) {
        BookStatusEnum bookStatusEnum;
        try {
            bookStatusEnum = BookStatusEnum.fromString(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status, e);
        }

        BookStatus bookStatus = bookStatusRepository.findByBookId(bookId);
        if (bookStatus != null) {
            bookStatus.setStatus(bookStatusEnum);
            if (BookStatusEnum.CHECKED_OUT.equals(bookStatusEnum)) {
                bookStatus.setBorrowedAt(LocalDateTime.now());
                bookStatus.setReturnBy(LocalDateTime
                        .now()
                        .plusWeeks(2)
                );
            } else if (BookStatusEnum.AVAILABLE.equals(bookStatusEnum)) {
                bookStatus.setBorrowedAt(null);
                bookStatus.setReturnBy(null);
            }
            bookStatusRepository.save(bookStatus);
        }
    }

    public void deleteBookStatus(String bookId) {
        BookStatus bookStatus = bookStatusRepository.findByBookId(bookId);
        if (bookStatus != null) {
            bookStatusRepository.delete(bookStatus);
        }
    }
}