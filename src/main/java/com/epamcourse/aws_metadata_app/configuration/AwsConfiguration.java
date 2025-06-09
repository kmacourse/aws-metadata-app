package com.epamcourse.aws_metadata_app.configuration;

import static com.amazonaws.regions.Regions.fromName;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@EnableScheduling
public class AwsConfiguration {

    @Bean
    public AmazonS3 amazonS3(@Value("${aws.region}") String bucketRegion) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(fromName(bucketRegion))
                .build();
    }

    @Bean
    public SnsClient snsClient(@Value("${aws.region}") String region) {
        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public SqsClient sqsClient(@Value("${aws.region}") String region) {
        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public LambdaClient lambdaClient(@Value("${aws.region}") String region) {
        return LambdaClient.builder()
                .region(Region.of(region))
                .build();
    }
}
