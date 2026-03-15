package com.sashank.payment.payment.client;

import com.sashank.payment.payment.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/notify/payment")
    void sendNotification(@RequestBody NotificationRequest request);
}