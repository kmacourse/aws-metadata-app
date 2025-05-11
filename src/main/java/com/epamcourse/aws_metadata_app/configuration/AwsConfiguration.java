package com.epamcourse.aws_metadata_app.configuration;

import static com.amazonaws.regions.Regions.fromName;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@EnableScheduling
public class AwsConfiguration {

    @Bean
    public AmazonS3 amazonS3(@Value("${aws.mentoring.bucket-region}") String bucketRegion) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(fromName(bucketRegion))
                .build();
    }
}
