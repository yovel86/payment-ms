package com.projects.payment_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private long orderId;
    private double amount;
    private Date paymentDate;
    private String transactionId;
    private PaymentStatus paymentStatus;
}
