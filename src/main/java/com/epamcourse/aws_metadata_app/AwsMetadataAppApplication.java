package com.epamcourse.aws_metadata_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AwsMetadataAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(AwsMetadataAppApplication.class, args);
	}

}
