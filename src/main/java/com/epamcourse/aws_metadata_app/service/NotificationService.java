package com.epamcourse.aws_metadata_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.ListSubscriptionsByTopicRequest;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.Subscription;
import software.amazon.awssdk.services.sns.model.UnsubscribeRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sns.model.Topic;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SnsClient snsClient;
    private final SqsClient sqsClient;
    @Value("${aws.sns.topic-name}")
    private String snsTopicName;
    @Value("${aws.sqs.queue-name}")
    private String sqsQueueName;
    @Value("${base.url}")
    private String baseUrl;

    private String getTopicArn() {

        return snsClient.listTopics()
                .topics()
                .stream()
                .filter(t -> t.topicArn().endsWith(":" + snsTopicName))
                .findFirst()
                .map(Topic::topicArn)
                .orElseThrow(() -> new RuntimeException("SNS Topic not found"));
    }

    private String getQueueUrl() {

        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(sqsQueueName).build()).queueUrl();
    }

    public ResponseEntity<?> subscribeToNotifications(String email) {

        SubscribeRequest request = SubscribeRequest.builder()
                .topicArn(getTopicArn())
                .protocol("email")
                .endpoint(email)
                .build();
        snsClient.subscribe(request);
        return ResponseEntity.ok("Confirmation email sent to " + email);
    }

    public ResponseEntity<?> unsubscribeFromNotifications(String email) {

        List<Subscription> subscriptions = snsClient.listSubscriptionsByTopic(
                ListSubscriptionsByTopicRequest.builder().topicArn(getTopicArn()).build()
        ).subscriptions();

        subscriptions.stream()
                .filter(s -> s.endpoint().equals(email))
                .findFirst()
                .ifPresent(sub -> snsClient.unsubscribe(
                        UnsubscribeRequest.builder().subscriptionArn(sub.subscriptionArn()).build()));

        return ResponseEntity.ok("Unsubscribed " + email);
    }

    public void processQueueAndSendToSns() {

        String queueUrl = getQueueUrl();
        String topicArn = getTopicArn();

        ReceiveMessageResponse messages = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(10)
                .build());

        for (Message msg : messages.messages()) {
            String body = msg.body();

            snsClient.publish(PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(body)
                    .build());

            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(msg.receiptHandle())
                    .build());
        }
    }

    public void sendImageUploadNotification(String fileName, long size, String extension) {

        String downloadUrl = baseUrl + "/v1/images/download/" + fileName;

        String message = String.format("""
                An image has been uploaded
                Name: %s
                Size: %d bytes
                Extension: %s
                Download: %s
                """, fileName, size, extension, downloadUrl);

        sendMessageToQueue(message);
    }

    private void sendMessageToQueue(String message) {

        String queueUrl = getQueueUrl();
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build());
    }
}