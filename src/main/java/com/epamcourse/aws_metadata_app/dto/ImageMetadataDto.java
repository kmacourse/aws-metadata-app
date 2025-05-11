package com.epamcourse.aws_metadata_app.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ImageMetadataDto {
    private Long id;
    private String name;
    private Long size;
    private String fileExtension;
    private LocalDateTime lastUpdate;
}
