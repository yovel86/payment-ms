package com.projects.payment_service.services;

import com.projects.payment_service.exceptions.InvalidOrderException;
import com.projects.payment_service.exceptions.InvalidUserException;

public interface PaymentService {

    String createPaymentLink(long userId, long orderId) throws InvalidUserException, InvalidOrderException, Exception;

}
