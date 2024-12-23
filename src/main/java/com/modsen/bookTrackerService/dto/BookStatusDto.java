package com.modsen.bookTrackerService.dto;

import com.modsen.bookTrackerService.model.BookStatusEnum;

import java.time.LocalDateTime;

public record BookStatusDto(String bookId, BookStatusEnum status, LocalDateTime borrowedAt, LocalDateTime returnBy) {
}
