package com.modsen.bookTrackerService.service;

import com.modsen.bookTrackerService.dto.BookStatusDto;
import com.modsen.bookTrackerService.exception.BookNotFoundException;
import com.modsen.bookTrackerService.model.BookStatus;
import com.modsen.bookTrackerService.model.BookStatusEnum;
import com.modsen.bookTrackerService.repository.BookStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookTrackerService {

    private final BookStatusRepository bookStatusRepository;

    public void createBookStatus(String bookId) {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);
        bookStatus.setStatus(BookStatusEnum.AVAILABLE);
        bookStatusRepository.save(bookStatus);
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

    public void updateBookStatus(String bookId, BookStatusEnum status) {
        BookStatus bookStatus = bookStatusRepository.findByBookId(bookId);
        if (bookStatus != null) {
            bookStatus.setStatus(status);
            if (BookStatusEnum.CHECKED_OUT.equals(status)) {
                bookStatus.setBorrowedAt(LocalDateTime.now());
                bookStatus.setReturnBy(LocalDateTime
                        .now()
                        .plusWeeks(2)
                );
            } else if (BookStatusEnum.AVAILABLE.equals(status)) {
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

    public BookStatus getStatus(String bookId) {
        return bookStatusRepository.findStatusByBookId(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with bookId: " + bookId));
    }
}