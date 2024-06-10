package com.projects.payment_service.models;

import jakarta.persistence.*;
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
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;
}
