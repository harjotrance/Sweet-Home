package com.upgrad.BookingService.exceptions;

public class BookingIdNotPresentException extends Exception{
    public BookingIdNotPresentException(String message) {
        super(message);
    }
}
