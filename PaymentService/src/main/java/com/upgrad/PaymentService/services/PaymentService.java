package com.upgrad.PaymentService.services;

import com.upgrad.PaymentService.dto.PaymentDTO;
import com.upgrad.PaymentService.entities.TransactionDetailsEntity;


public interface PaymentService {
    public int acceptPaymentDetails(PaymentDTO paymentDTO);

    public TransactionDetailsEntity getTransactionDetails(int transactionId) throws Exception;
}
