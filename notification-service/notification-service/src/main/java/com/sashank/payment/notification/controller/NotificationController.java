package com.sashank.payment.notification.controller;

import com.sashank.payment.notification.dto.NotificationRequest;
import com.sashank.payment.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/payment")
    public String sendPaymentNotification(@RequestBody NotificationRequest request) {

        notificationService.sendPaymentEmail(request);

        return "Notification Sent";
    }
}