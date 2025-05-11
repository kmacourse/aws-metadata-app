package com.epamcourse.aws_metadata_app.controller;

import static io.netty.util.internal.StringUtil.EMPTY_STRING;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epamcourse.aws_metadata_app.dto.ImageUploadDto;
import com.epamcourse.aws_metadata_app.service.ImageService;


@RestController
@CrossOrigin
@RequestMapping("v1/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getImageMetadata(
            @RequestParam(name = "name", required = false, defaultValue = EMPTY_STRING) String name,
            @RequestParam(name = "isRandom", required = false, defaultValue = "false") Boolean isRandom) {
        if (TRUE.equals(isRandom)) {
            return imageService.findRandomImageMetadata();
        }
        return imageService.findImageMetadataByName(name);
    }

    @GetMapping(value = "/{name}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getImage(@PathVariable String name) {
        return imageService.findImage(name);
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(@ModelAttribute ImageUploadDto downloadClientModel) {
        return imageService.upload(downloadClientModel);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> delete(@PathVariable String name) {
        return imageService.delete(name);
    }
}
