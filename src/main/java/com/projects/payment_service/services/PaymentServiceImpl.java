package com.projects.payment_service.services;

import com.projects.payment_service.dtos.User;
import com.projects.payment_service.exceptions.InvalidOrderException;
import com.projects.payment_service.exceptions.InvalidUserException;
import com.projects.payment_service.models.Payment;
import com.projects.payment_service.models.PaymentStatus;
import com.projects.payment_service.payment_gateways.PaymentGatewayAdapter;
import com.projects.payment_service.repositories.PaymentRepository;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${RAZORPAY_KEY_SECRET}")
    private String razorpayKeySecret;

    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayAdapter paymentGatewayAdapter;
    private final String USER_SERVICE_URL = "http://localhost:8080/users";
    private final String ORDER_SERVICE_URL = "http://localhost:8082/orders";

    @Autowired
    public PaymentServiceImpl(RestTemplate restTemplate, PaymentRepository paymentRepository, PaymentGatewayAdapter paymentGatewayAdapter) {
        this.restTemplate = restTemplate;
        this.paymentRepository = paymentRepository;
        this.paymentGatewayAdapter = paymentGatewayAdapter;
    }

    @Override
    public String createPaymentLink(long userId, long orderId) throws Exception {
        User user = this.restTemplate.getForObject(USER_SERVICE_URL + "/" + userId, User.class);
        if(user == null) throw new InvalidUserException("Invalid User");
        Double amount = this.restTemplate.getForObject(ORDER_SERVICE_URL + "/" + orderId + "/amount", Double.class);
        if(amount == null) throw new InvalidOrderException("Invalid Order");
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        this.paymentRepository.save(payment);
        return this.paymentGatewayAdapter.createPaymentLink(user, orderId, amount);
    }

    @Override
    public String handlePaymentCallback(String paymentId, String paymentLinkId, String paymentLinkReferenceId, String paymentLinkStatus, String signature, long orderId, long userId) throws Exception {
        Optional<Payment> paymentOptional = this.paymentRepository.findPaymentByQuery(userId, orderId, PaymentStatus.PENDING);
        if(paymentOptional.isEmpty()) return "Payment Not found";
        Payment payment = paymentOptional.get();
        boolean isSignatureValid = verifySignature(paymentLinkReferenceId, paymentId, signature);
        if(!isSignatureValid) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            this.paymentRepository.save(payment);
            return "Signature not matching, Payment failed";
        }
        if(paymentLinkStatus.equals("paid")) {
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            payment.setPaymentDate(new Date());
            this.paymentRepository.save(payment);
            return "Payment completed";
        }
        payment.setPaymentStatus(PaymentStatus.FAILED);
        this.paymentRepository.save(payment);
        return "Payment Failed";
    }

    private boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            Utils.verifyWebhookSignature(payload, signature, razorpayKeySecret);
            return true;
        } catch (RazorpayException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
