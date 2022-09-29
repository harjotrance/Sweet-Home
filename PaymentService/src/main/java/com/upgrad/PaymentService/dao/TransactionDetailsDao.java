package com.upgrad.PaymentService.dao;

import com.upgrad.PaymentService.entities.TransactionDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailsDao extends JpaRepository<TransactionDetailsEntity, Integer> {

}
