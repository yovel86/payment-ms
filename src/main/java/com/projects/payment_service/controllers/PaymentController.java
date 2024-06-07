package com.projects.payment_service.controllers;

import com.projects.payment_service.dtos.CreatePaymentLinkRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<String> createPaymentLink(CreatePaymentLinkRequestDTO requestDTO) {
        return null;
    }

}
