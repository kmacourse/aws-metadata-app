package com.epamcourse.aws_metadata_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epamcourse.aws_metadata_app.service.MetadataService;

@RestController
public class MetadataController {
    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping("/metadata")
    public String getMetadata() {
        return metadataService.getRegionAndAZ();
    }
}
