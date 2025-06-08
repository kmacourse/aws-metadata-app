//package com.epamcourse.aws_metadata_app.queue;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import com.epamcourse.aws_metadata_app.service.NotificationService;
//
//import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class QueueProcessor {
//
//    private final NotificationService notificationService;
//
//    @Scheduled(fixedDelay = 60000)
//    public void processMessages() {
//        notificationService.processQueueAndSendToSns();
//    }
//}