package com.modsen.bookTrackerService.models;

public enum BookStatusEnum {
    AVAILABLE,
    CHECKED_OUT;

    public static BookStatusEnum fromString(String status) {
        try {
            return BookStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}
