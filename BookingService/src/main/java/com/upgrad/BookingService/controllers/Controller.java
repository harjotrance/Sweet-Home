package com.upgrad.BookingService.controllers;
import com.upgrad.BookingService.dto.BookingDTO;
import com.upgrad.BookingService.dto.ExceptionDTO;
import com.upgrad.BookingService.dto.PaymentDTO;
import com.upgrad.BookingService.exceptions.BookingIdNotPresentException;
import com.upgrad.BookingService.services.BookingService;
import com.upgrad.BookingService.entities.BookingInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//The controller for Booking Microservice
@RestController
@RequestMapping(value = "/hotel")
public class Controller {

    //Injecting the Service Class Object
    @Autowired
    private BookingService bookingService;

    //EndPoint 1 of the Booking Service
    @PostMapping(value = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity bookingDetails(@RequestBody BookingDTO bookingDTO) {
        BookingInfoEntity b = bookingService.acceptBookingDetails(bookingDTO);
        return new ResponseEntity(b, HttpStatus.CREATED);
    }

    //Endpoint 2 of the Booking Service
    @PostMapping(value = "/booking/{id}/transaction", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity bookingConfirmation(@RequestBody PaymentDTO paymentDTO, @PathVariable(name = "id") int id) {
        if(paymentDTO.getBookingId()!=id)
            return new ResponseEntity("Booking Id in URI and Request Body don't match", HttpStatus.BAD_REQUEST);
        String paymentMode = paymentDTO.getPaymentMode();
        if (paymentMode.equals("CARD") || paymentMode.equals("UPI")) {
            try {
                BookingInfoEntity b = bookingService.acceptPaymentDetails(paymentDTO);
                return new ResponseEntity(b, HttpStatus.CREATED);
            }

            catch (BookingIdNotPresentException e) {
                //Exception 1 : "Invalid Booking Id"
                ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage(), 400);
                return new ResponseEntity(exceptionDTO, HttpStatus.BAD_REQUEST);
            }
        }

        else {
            //Exception 2 : "Invalid mode of payment"
            ExceptionDTO exceptionDTO = new ExceptionDTO("Invalid mode of payment", 400);
            return new ResponseEntity(exceptionDTO, HttpStatus.BAD_REQUEST);
        }

    }
}
