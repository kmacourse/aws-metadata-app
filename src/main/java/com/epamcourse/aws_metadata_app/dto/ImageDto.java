package com.epamcourse.aws_metadata_app.dto;

import lombok.Data;

@Data
public class ImageDto {
    private ImageMetadataDto metadata;
    private byte[] file;
}
