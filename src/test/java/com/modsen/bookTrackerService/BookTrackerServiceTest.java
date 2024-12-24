package com.modsen.bookTrackerService;

import com.modsen.bookTrackerService.dto.BookStatusDto;
import com.modsen.bookTrackerService.exception.BookNotFoundException;
import com.modsen.bookTrackerService.model.BookStatus;
import com.modsen.bookTrackerService.model.BookStatusEnum;
import com.modsen.bookTrackerService.repository.BookStatusRepository;
import com.modsen.bookTrackerService.service.BookTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookTrackerServiceTest {

    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private BookTrackerService bookTrackerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBookStatus() {
        String bookId = "1";
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);
        bookStatus.setStatus(BookStatusEnum.AVAILABLE);

        bookTrackerService.createBookStatus(bookId);

        verify(bookStatusRepository, times(1)).save(any(BookStatus.class));
    }

    @Test
    void getBooksByStatus() {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId("1");
        bookStatus.setStatus(BookStatusEnum.AVAILABLE);
        bookStatus.setBorrowedAt(LocalDateTime.now());
        bookStatus.setReturnBy(LocalDateTime.now().plusWeeks(2));

        when(bookStatusRepository.findByStatus(BookStatusEnum.AVAILABLE))
                .thenReturn(Arrays.asList(bookStatus));

        List<BookStatusDto> result = bookTrackerService.getBooksByStatus(BookStatusEnum.AVAILABLE);

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).bookId());
        verify(bookStatusRepository, times(1)).findByStatus(BookStatusEnum.AVAILABLE);
    }

    @Test
    void updateBookStatus() {
        String bookId = "1";
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);
        bookStatus.setStatus(BookStatusEnum.AVAILABLE);

        when(bookStatusRepository.findByBookId(bookId)).thenReturn(bookStatus);

        bookTrackerService.updateBookStatus(bookId, BookStatusEnum.CHECKED_OUT);

        assertEquals(BookStatusEnum.CHECKED_OUT, bookStatus.getStatus());
        assertNotNull(bookStatus.getBorrowedAt());
        assertNotNull(bookStatus.getReturnBy());
        verify(bookStatusRepository, times(1)).save(bookStatus);
    }

    @Test
    void deleteBookStatus() {
        String bookId = "1";
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);

        when(bookStatusRepository.findByBookId(bookId)).thenReturn(bookStatus);

        bookTrackerService.deleteBookStatus(bookId);

        verify(bookStatusRepository, times(1)).delete(bookStatus);
    }

    @Test
    void getStatus() {
        String bookId = "1";
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);
        bookStatus.setStatus(BookStatusEnum.AVAILABLE);

        when(bookStatusRepository.findStatusByBookId(bookId)).thenReturn(Optional.of(bookStatus));

        BookStatus result = bookTrackerService.getStatus(bookId);

        assertEquals(bookStatus, result);
        verify(bookStatusRepository, times(1)).findStatusByBookId(bookId);
    }

    @Test
    void getStatus_BookNotFoundException() {
        String bookId = "1";

        when(bookStatusRepository.findStatusByBookId(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookTrackerService.getStatus(bookId));
        verify(bookStatusRepository, times(1)).findStatusByBookId(bookId);
    }
}
