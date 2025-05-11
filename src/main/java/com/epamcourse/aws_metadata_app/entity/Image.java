package com.epamcourse.aws_metadata_app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image")
@Getter
@Setter
@RequiredArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "image_size")
    private Long size;

    @Column(name = "file_extension", length = 40)
    private String fileExtension;

    @Column(name = "last_update",
            columnDefinition = "TIMESTAMP",
            insertable = false,
            updatable = false)
    private LocalDateTime lastUpdate;
}
