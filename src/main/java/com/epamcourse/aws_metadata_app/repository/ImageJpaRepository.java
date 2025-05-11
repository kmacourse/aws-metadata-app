package com.epamcourse.aws_metadata_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.epamcourse.aws_metadata_app.entity.Image;

@Repository
public interface ImageJpaRepository extends JpaRepository<Image, Long> {

    List<Image> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM image ORDER BY RAND() limit 1;")
    Optional<Image> findRandom();
}
