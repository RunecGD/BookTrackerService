package com.modsen.bookTrackerService;
import com.modsen.bookTrackerService.dto.BookStatusDto;
import com.modsen.bookTrackerService.models.BookStatus;
import com.modsen.bookTrackerService.models.BookStatusEnum;
import com.modsen.bookTrackerService.repository.BookStatusRepository;
import com.modsen.bookTrackerService.service.BookTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookTrackerServiceTest {

	@Mock
	private BookStatusRepository bookStatusRepository;

	@InjectMocks
	private BookTrackerService bookTrackerService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateBookStatus() {
		
		String bookId = "1234";
		BookStatus bookStatus = new BookStatus();
		bookStatus.setBookId(bookId);
		bookStatus.setStatus(BookStatusEnum.AVAILABLE);

		when(bookStatusRepository.save(any(BookStatus.class))).thenReturn(bookStatus);

		
		BookStatus result = bookTrackerService.createBookStatus(bookId);

		
		assertEquals(bookId, result.getBookId());
		assertEquals(BookStatusEnum.AVAILABLE, result.getStatus());
		verify(bookStatusRepository).save(any(BookStatus.class));
	}

	@Test
	public void testGetBooksByStatus() {
		
		String bookId = "1234";
		BookStatus bookStatus = new BookStatus();
		bookStatus.setBookId(bookId);
		bookStatus.setStatus(BookStatusEnum.AVAILABLE);
		bookStatus.setBorrowedAt(null); 
		bookStatus.setReturnBy(null); 

		when(bookStatusRepository.findByStatus(BookStatusEnum.AVAILABLE))
				.thenReturn(Collections.singletonList(bookStatus));

		
		List<BookStatusDto> result = bookTrackerService.getBooksByStatus(BookStatusEnum.AVAILABLE);

		
		assertEquals(1, result.size());
		assertEquals(bookId, result.get(0).bookId()); 
		assertEquals(BookStatusEnum.AVAILABLE, result.get(0).status()); 
		assertNull(result.get(0).borrowedAt()); 
		assertNull(result.get(0).returnBy()); 
	}

	@Test
	public void testUpdateBookStatus() {
		
		String bookId = "1234";
		BookStatus existingStatus = new BookStatus();
		existingStatus.setBookId(bookId);
		existingStatus.setStatus(BookStatusEnum.AVAILABLE);

		when(bookStatusRepository.findByBookId(bookId)).thenReturn(existingStatus);

		
		bookTrackerService.updateBookStatus(bookId, "CHECKED_OUT");

		
		assertEquals(BookStatusEnum.CHECKED_OUT, existingStatus.getStatus());
		assertNotNull(existingStatus.getBorrowedAt());
		assertNotNull(existingStatus.getReturnBy());
		verify(bookStatusRepository).save(existingStatus);
	}

	@Test
	public void testUpdateBookStatus_InvalidStatus() {
		
		String bookId = "1234";

		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			bookTrackerService.updateBookStatus(bookId, "INVALID_STATUS");
		});
		assertEquals("Invalid status: INVALID_STATUS", exception.getMessage());
	}

	@Test
	public void testDeleteBookStatus() {
		
		String bookId = "1234";
		BookStatus bookStatus = new BookStatus();
		bookStatus.setBookId(bookId);

		when(bookStatusRepository.findByBookId(bookId)).thenReturn(bookStatus);

		
		bookTrackerService.deleteBookStatus(bookId);

		
		verify(bookStatusRepository).delete(bookStatus);
	}

	@Test
	public void testDeleteBookStatus_NotFound() {
		
		String bookId = "1234";

		when(bookStatusRepository.findByBookId(bookId)).thenReturn(null);

		
		bookTrackerService.deleteBookStatus(bookId);

		
		verify(bookStatusRepository, never()).delete(any(BookStatus.class));
	}
}
