package com.projects.payment_service.payment_gateways;

import com.projects.payment_service.dtos.User;

public interface PaymentGatewayAdapter {

    String createPaymentLink(User user, long orderId, double amount) throws Exception;

}
