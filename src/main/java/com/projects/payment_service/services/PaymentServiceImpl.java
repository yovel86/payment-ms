package com.projects.payment_service.services;

import com.projects.payment_service.dtos.User;
import com.projects.payment_service.exceptions.InvalidOrderException;
import com.projects.payment_service.exceptions.InvalidUserException;
import com.projects.payment_service.payment_gateways.PaymentGatewayAdapter;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${RAZORPAY_KEY_SECRET}")
    private String razorpayKeySecret;

    private final RestTemplate restTemplate;
    private final PaymentGatewayAdapter paymentGatewayAdapter;
    private final String USER_SERVICE_URL = "http://localhost:8080/users";
    private final String ORDER_SERVICE_URL = "http://localhost:8082/orders";

    @Autowired
    public PaymentServiceImpl(PaymentGatewayAdapter paymentGatewayAdapter, RestTemplate restTemplate) {
        this.paymentGatewayAdapter = paymentGatewayAdapter;
        this.restTemplate = restTemplate;
    }

    @Override
    public String createPaymentLink(long userId, long orderId) throws InvalidUserException, InvalidOrderException, Exception {
        // Validate User & Order
        // Get the order amount
        // Pass the User & Order details to Service
        User user = this.restTemplate.getForObject(USER_SERVICE_URL + "/" + userId, User.class);
        if(user == null) throw new InvalidUserException("Invalid User");
        return this.paymentGatewayAdapter.createPaymentLink(user, orderId, 10);
    }

    @Override
    public String handlePaymentCallback(String paymentId, String paymentLinkId, String paymentLinkReferenceId, String paymentLinkStatus, String signature) throws Exception {
        boolean isSignatureValid = verifySignature(paymentLinkReferenceId, paymentId, signature);
        if(!isSignatureValid) return "Signature not matching, Payment failed";
        if(paymentLinkStatus.equals("paid")) return "Payment completed";
        return "Payment Failed";
    }

    private boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            return Utils.verifyWebhookSignature(payload, signature, razorpayKeySecret);
        } catch (RazorpayException e) {
            return false;
        }
    }

}
