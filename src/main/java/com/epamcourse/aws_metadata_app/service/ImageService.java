package com.epamcourse.aws_metadata_app.service;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.epamcourse.aws_metadata_app.dto.ImageMetadataDto;
import com.epamcourse.aws_metadata_app.dto.ImageUploadDto;
import com.epamcourse.aws_metadata_app.entity.Image;
import com.epamcourse.aws_metadata_app.mapper.ImageMapper;
import com.epamcourse.aws_metadata_app.repository.ImageJpaRepository;
import com.epamcourse.aws_metadata_app.exception.NotFoundException;


@Service
public class ImageService {

    private final ImageJpaRepository imageJpaRepository;
    private final ImageMapper imageMapper;
    private final S3Service s3Service;
    private final FileService fileService;
    private final NotificationService notificationService;

    @Autowired
    public ImageService(ImageJpaRepository imageJpaRepository,
            ImageMapper imageMapper,
            S3Service s3Service,
            FileService fileService,
            NotificationService notificationService) {
        this.imageJpaRepository = imageJpaRepository;
        this.imageMapper = imageMapper;
        this.s3Service = s3Service;
        this.fileService = fileService;
        this.notificationService = notificationService;
    }

    public ResponseEntity<?> findImageMetadataByName(String name) {
        Image image = imageJpaRepository.findByName(name).stream()
                .findFirst()
                .orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(imageMapper.toMetadataDto(image), OK);
    }

    public ResponseEntity<?> findRandomImageMetadata() {
        Image image = imageJpaRepository.findRandom().stream()
                .findFirst()
                .orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(imageMapper.toMetadataDto(image), OK);
    }

    @Transactional
    public ResponseEntity<?> findImage(String name) {
        Image image = imageJpaRepository.findByName(name).stream()
                .findFirst()
                .orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(imageMapper.toDto(image, s3Service), OK);
    }

    public byte[] downloadImageFile(String name) {
        return s3Service.downloadObject(name);
    }

    public String getContentType(String name) {
        Image image = imageJpaRepository.findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(NotFoundException::new);
        return switch (image.getFileExtension().toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
            case "png" -> MediaType.IMAGE_PNG_VALUE;
            case "gif" -> MediaType.IMAGE_GIF_VALUE;
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }

    @Transactional
    public ResponseEntity<?> upload(ImageUploadDto downloadClientModel) {
        MultipartFile file = downloadClientModel.getFile();
        InputStream inputStream = fileService.getInputStream(file);

        s3Service.uploadObject(inputStream, file.getOriginalFilename(), downloadClientModel.getName());

        Image image = imageMapper.toEntity(downloadClientModel);
        image.setSize(file.getSize());
        image.setFileExtension(fileService.getFileExtension(file.getOriginalFilename()));

        Image save = imageJpaRepository.save(image);
        ImageMetadataDto imageMetadataDto = imageMapper.toMetadataDto(save);

        notificationService.sendImageUploadNotification(
                save.getName(),
                save.getSize(),
                save.getFileExtension()
        );

        return new ResponseEntity<>(imageMetadataDto, CREATED);
    }

    @Transactional
    public ResponseEntity<?> delete(String name) {
        List<Image> imageList = imageJpaRepository.findByName(name);
        imageList.stream()
                .map(Image::getName)
                .forEach(s3Service::deleteObject);
        imageList.forEach(imageJpaRepository::delete);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
