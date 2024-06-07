package com.projects.payment_service.configs;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Value("${RAZORPAY_KEY_ID}")
    private String razorpayKeyId;
    @Value("${RAZORPAY_KEY_SECRET}")
    private String razorpayKeySecret;

    @Bean
    public RazorpayClient getRazorpayClient() throws RazorpayException {
        return new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }

}
