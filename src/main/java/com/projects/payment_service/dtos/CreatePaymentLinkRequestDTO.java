package com.projects.payment_service.dtos;

import lombok.Data;

@Data
public class CreatePaymentLinkRequestDTO {
    private long userId;
    private long orderId;
}
