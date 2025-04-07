package com.epamcourse.aws_metadata_app.service;

import org.springframework.stereotype.Service;

import com.amazonaws.util.EC2MetadataUtils;

@Service
public class MetadataService {
    public String getRegionAndAZ() {
        String az = EC2MetadataUtils.getAvailabilityZone();
        String region = az != null ? az.substring(0, az.length() - 1) : "unknown";
        return String.format("Region: %s, AZ: %s", region, az);
    }
}
