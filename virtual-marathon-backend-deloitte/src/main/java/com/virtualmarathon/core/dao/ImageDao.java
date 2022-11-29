package com.virtualmarathon.core.dao;

import com.virtualmarathon.core.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface ImageDao extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
}
