package com.modsen.book_tracker_service.service;

import com.modsen.book_tracker_service.models.BookStatus;
import com.modsen.book_tracker_service.repository.BookStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookTrackerService {

    @Autowired
    private BookStatusRepository bookStatusRepository;

    // Метод для создания записи статуса книги
    public BookStatus createBookStatus(String bookId) {
        BookStatus bookStatus = new BookStatus();
        bookStatus.setBookId(bookId);
        bookStatus.setStatus("available");

        // Сохраняем в базе данных
        return bookStatusRepository.save(bookStatus);
    }

    // Метод для получения списка свободных книг
    public List<BookStatus> getAvailableBooks() {
        return bookStatusRepository.findByStatus("available");
    }

    // Метод для обновления статуса книги
    public void updateBookStatus(String bookId, String status) {
        BookStatus bookStatus = bookStatusRepository.findByBookId(bookId);
        if (bookStatus != null) {
            bookStatus.setStatus(status);
            if ("borrowed".equals(status)) {
                bookStatus.setBorrowedAt(LocalDateTime.now());
                // Устанавливаем время возврата на 2 недели вперед
                bookStatus.setReturnBy(LocalDateTime.now().plusWeeks(2));
            } else if ("available".equals(status)) {
                // Если книга возвращается, сбрасываем время возврата
                bookStatus.setBorrowedAt(null);
                bookStatus.setReturnBy(null);
            }
            bookStatusRepository.save(bookStatus);
        }
    }

    // Метод для удаления записи о книге
    public void deleteBookStatus(String bookId) {
        BookStatus bookStatus = bookStatusRepository.findByBookId(bookId);
        if (bookStatus != null) {
            bookStatusRepository.delete(bookStatus);
        }
    }
}