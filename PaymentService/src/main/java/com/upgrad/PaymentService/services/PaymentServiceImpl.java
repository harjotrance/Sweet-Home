package com.upgrad.PaymentService.services;

import com.upgrad.PaymentService.dao.TransactionDetailsDao;
import com.upgrad.PaymentService.dto.PaymentDTO;
import com.upgrad.PaymentService.entities.TransactionDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private TransactionDetailsDao transactionDetailsDao;

    @Override
    public int acceptPaymentDetails(PaymentDTO paymentDTO) {
        TransactionDetailsEntity transactionDetailsEntity = new TransactionDetailsEntity();
        transactionDetailsEntity.setBookingId(paymentDTO.getBookingId());
        transactionDetailsEntity.setPaymentMode(paymentDTO.getPaymentMode());
        transactionDetailsEntity.setCardNumber(paymentDTO.getCardNumber());
        transactionDetailsEntity.setUpiId(paymentDTO.getUpiId());

        return transactionDetailsDao.save(transactionDetailsEntity).getId();
    }

    @Override
    public TransactionDetailsEntity getTransactionDetails(int transactionId) throws Exception {
        Optional<TransactionDetailsEntity> t = transactionDetailsDao.findById(transactionId);
        if(t.isPresent())
        return t.get();
        else throw new Exception("Transaction number " + transactionId + " does not exist");
    }
}
