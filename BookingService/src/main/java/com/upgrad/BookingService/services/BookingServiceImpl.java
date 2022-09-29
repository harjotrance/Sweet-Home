package com.upgrad.BookingService.services;

import com.upgrad.BookingService.dao.BookingInfoDao;
import com.upgrad.BookingService.dto.BookingDTO;
import com.upgrad.BookingService.dto.PaymentDTO;
import com.upgrad.BookingService.entities.BookingInfoEntity;
import com.upgrad.BookingService.exceptions.BookingIdNotPresentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Service
public class BookingServiceImpl implements BookingService{
    @Autowired
    private BookingInfoDao bookingInfoDao;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${paymentService.url}")
    private String paymentServiceUrl;

    //Utility Method to get room numbers based on count
    public static String getRandomNumbers(int count){
        Random rand = new Random();
        int upperBound = 100;
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(rand.nextInt(upperBound));

        for (int i=1; i<count; i++){
            stringBuilder.append(',');
            stringBuilder.append(rand.nextInt(upperBound));
        }

        return stringBuilder.toString();
    }

    //Utility method to convert String to LocalDate
    public static LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }


    //Accepts Booking request. Calculates total Room Price, gets room numbers, and sets bookedOn
    @Override
    public BookingInfoEntity acceptBookingDetails(BookingDTO bookingDTO) {
        String roomNumbers = getRandomNumbers(bookingDTO.getNumOfRooms());
        LocalDate fromDate = stringToLocalDate(bookingDTO.getFromDate());
        LocalDate toDate = stringToLocalDate(bookingDTO.getToDate());
        int numDays = (int) Duration.between(fromDate.atStartOfDay(), toDate.atStartOfDay()).toDays();
        int roomRate = 1000;
        int roomPrice = roomRate * bookingDTO.getNumOfRooms() * numDays;
        ZonedDateTime bookedOn = ZonedDateTime.now();

        BookingInfoEntity bookingInfo = new BookingInfoEntity();
        bookingInfo.setBookedOn(bookedOn);
        bookingInfo.setAadharNumber(bookingDTO.getAadharNumber());
        bookingInfo.setFromDate(fromDate.atStartOfDay().atZone(ZoneId.of("Asia/Kolkata")));
        bookingInfo.setToDate(toDate.atStartOfDay().atZone(ZoneId.of("Asia/Kolkata")));
        bookingInfo.setNumOfRooms(bookingDTO.getNumOfRooms());
        bookingInfo.setRoomNumbers(roomNumbers);
        bookingInfo.setRoomPrice(roomPrice);

        return  bookingInfoDao.save(bookingInfo);
    }

    //If relevant bookingId is found, calls Payment Service Endpoint 1 and prints confirmation message
    @Override
    public BookingInfoEntity acceptPaymentDetails(PaymentDTO paymentDTO) throws BookingIdNotPresentException {
        Optional<BookingInfoEntity> b = bookingInfoDao.findById(paymentDTO.getBookingId());
        if(b.isEmpty()) throw new BookingIdNotPresentException("Invalid Booking Id");

        BookingInfoEntity bookingInfoEntity = b.get();

        Integer transactionId = restTemplate.postForObject(paymentServiceUrl, paymentDTO, Integer.class);
        if(transactionId==null){
            return null;
        }

        bookingInfoEntity.setTransactionId(transactionId);
        bookingInfoEntity = bookingInfoDao.save(bookingInfoEntity);
        String message = "Booking confirmed for user with aadhaar number: "
                + bookingInfoEntity.getAadharNumber()
                +    "    |    "
                + "Here are the booking details:    " + bookingInfoEntity;

        System.out.println(message);
        return bookingInfoEntity;
    }
}
