package com.projects.payment_service.controllers;

import com.projects.payment_service.dtos.CreatePaymentLinkRequestDTO;
import com.projects.payment_service.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> createPaymentLink(@RequestBody CreatePaymentLinkRequestDTO requestDTO) {
        long userId = requestDTO.getUserId();
        long orderId = requestDTO.getOrderId();
        try {
            String paymentLink = this.paymentService.createPaymentLink(userId, orderId);
            return ResponseEntity.ok(paymentLink);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
