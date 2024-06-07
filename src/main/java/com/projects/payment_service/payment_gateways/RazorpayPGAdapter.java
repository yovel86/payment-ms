package com.projects.payment_service.payment_gateways;

import com.projects.payment_service.dtos.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RazorpayPGAdapter implements PaymentGatewayAdapter {

    private final RazorpayClient razorpayClient;

    @Autowired
    public RazorpayPGAdapter(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String createPaymentLink(User user, long orderId, double amount) throws RazorpayException {
        JSONObject paymentLinkRequest = new JSONObject();
        long convertedAmount = Math.round(amount * 100);
        paymentLinkRequest.put("amount", convertedAmount); // 10rs (10.00) - need to convert amount from 'double' to 'long'
        paymentLinkRequest.put("currency", "INR");
        long expire_by = Instant.ofEpochMilli(System.currentTimeMillis()).plusSeconds(30 * 60).toEpochMilli(); // Add 30 minutes
        paymentLinkRequest.put("expire_by", expire_by);
        paymentLinkRequest.put("reference_id", String.valueOf(orderId));
        paymentLinkRequest.put("description","Payment for Order with ID: " + orderId);

        JSONObject customer = new JSONObject();
        customer.put("name", user.getName());
        customer.put("email", user.getEmail());
        paymentLinkRequest.put("customer",customer);

        JSONObject notify = new JSONObject();
        notify.put("email",true);
        paymentLinkRequest.put("notify",notify);

        paymentLinkRequest.put("reminder_enable",true);
        paymentLinkRequest.put("callback_url","http://localhost:8083/payments/callback");
        paymentLinkRequest.put("callback_method","get");
        PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
        System.out.println(paymentLink);
        return paymentLink.get("short_url");
    }

}
