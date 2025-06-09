package com.epamcourse.aws_metadata_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@RestController
@RequestMapping("/v1/consistency")
@RequiredArgsConstructor
public class ConsistencyController {

    private final LambdaClient lambdaClient;

    @PostMapping("/check")
    public ResponseEntity<?> checkConsistency() {
        try {
            InvokeRequest request = InvokeRequest.builder()
                    .functionName("EpamCourse-DataConsistencyFunction")
                    .payload(SdkBytes.fromUtf8String("{\"detail-type\": \"Web Application Call\"}"))
                    .build();

            InvokeResponse response = lambdaClient.invoke(request);
            String result = new String(response.payload().asByteArray());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error invoking Lambda: " + e.getMessage());
        }
    }
}
