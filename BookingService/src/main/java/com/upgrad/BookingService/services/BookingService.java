package com.upgrad.BookingService.services;

import com.upgrad.BookingService.dto.BookingDTO;
import com.upgrad.BookingService.dto.PaymentDTO;
import com.upgrad.BookingService.entities.BookingInfoEntity;
import com.upgrad.BookingService.exceptions.BookingIdNotPresentException;


public interface BookingService {
    public BookingInfoEntity acceptBookingDetails(BookingDTO bookingDTO);

    public BookingInfoEntity acceptPaymentDetails(PaymentDTO paymentDTO) throws BookingIdNotPresentException;
}
