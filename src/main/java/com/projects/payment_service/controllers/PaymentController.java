package com.projects.payment_service.controllers;

import com.projects.payment_service.dtos.CreatePaymentLinkRequestDTO;
import com.projects.payment_service.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/callback")
    public ResponseEntity<String> handlePaymentCallback(
            @RequestParam(value = "razorpay_payment_id") String paymentId,
            @RequestParam(value = "razorpay_payment_link_id") String paymentLinkId,
            @RequestParam(value = "razorpay_payment_link_reference_id") String paymentLinkReferenceId,
            @RequestParam(value = "razorpay_payment_link_status") String paymentLinkStatus,
            @RequestParam(value = "razorpay_signature") String signature,
            @RequestParam(value = "orderId") long orderId,
            @RequestParam(value = "userId") long userId
    ) {
        if(paymentId == null || paymentLinkId == null || paymentLinkReferenceId == null || paymentLinkStatus == null || signature == null) {
            return new ResponseEntity<>("FAILURE", HttpStatus.BAD_REQUEST);
        }
        try {
            String status = this.paymentService.handlePaymentCallback(paymentId, paymentLinkId, paymentLinkReferenceId, paymentLinkStatus, signature, orderId, userId);
            System.out.println(status);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
