package com.epamcourse.aws_metadata_app.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageUploadDto {
    @NotEmpty
    private String name;
    @NotNull
    private MultipartFile file;
}
