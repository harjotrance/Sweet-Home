package com.upgrad.BookingService.dao;
import com.upgrad.BookingService.entities.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingInfoDao extends JpaRepository<BookingInfoEntity, Integer> {

}
