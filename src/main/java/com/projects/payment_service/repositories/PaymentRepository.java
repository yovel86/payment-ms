package com.projects.payment_service.repositories;

import com.projects.payment_service.models.Payment;
import com.projects.payment_service.models.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.userId=:userId and p.orderId=:orderId and p.paymentStatus=:status")
    Optional<Payment> findPaymentByQuery(long userId, long orderId, PaymentStatus status);

}
