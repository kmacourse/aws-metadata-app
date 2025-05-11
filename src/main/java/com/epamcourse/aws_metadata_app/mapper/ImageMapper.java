package com.epamcourse.aws_metadata_app.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.epamcourse.aws_metadata_app.dto.ImageDto;
import com.epamcourse.aws_metadata_app.dto.ImageMetadataDto;
import com.epamcourse.aws_metadata_app.dto.ImageUploadDto;
import com.epamcourse.aws_metadata_app.entity.Image;
import com.epamcourse.aws_metadata_app.service.S3Service;


@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "file", ignore = true)
    ImageDto toDto(Image entityModel, @Context S3Service s3Service);

    ImageMetadataDto toMetadataDto(Image entityModel);

    Image toEntity(ImageUploadDto clientModel);

    @AfterMapping
    default void setMetadata(@MappingTarget ImageDto target, Image source) {
        target.setMetadata(toMetadataDto(source));
    }

    @AfterMapping
    default void setBitmapToClient(@MappingTarget ImageDto target, Image source, @Context S3Service s3Service) {
        target.setFile(s3Service.downloadObject(source.getName()));
    }
}
