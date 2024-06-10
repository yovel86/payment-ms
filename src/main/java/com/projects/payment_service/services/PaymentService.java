package com.projects.payment_service.services;

public interface PaymentService {

    String createPaymentLink(long userId, long orderId) throws Exception;

    String handlePaymentCallback(String paymentId, String paymentLinkId, String paymentLinkReferenceId, String paymentLinkStatus, String signature, long orderId, long userId) throws Exception;

}
