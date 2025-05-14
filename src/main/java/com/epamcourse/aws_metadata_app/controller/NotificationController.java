package com.epamcourse.aws_metadata_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epamcourse.aws_metadata_app.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/subscriptions/{email}")
    public ResponseEntity<?> subscribeToNotifications(@PathVariable  String email) {
        return notificationService.subscribeToNotifications(email);
    }

    @DeleteMapping("/subscriptions/{email}")
    public ResponseEntity<?> unsubscribeFromNotifications(@PathVariable String email) {
        return notificationService.unsubscribeFromNotifications(email);
    }
}
