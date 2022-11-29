package com.virtualmarathon.core.controller;

import com.virtualmarathon.core.entity.Image;
import com.virtualmarathon.core.response.ImageUploadResponse;
import com.virtualmarathon.core.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("${cors.urls}")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload/image")
    @PreAuthorize("hasRole('organizer')")
    public ResponseEntity<ImageUploadResponse> uplaodImage(@RequestParam("image") MultipartFile file) throws Exception {
        return imageService.uplaodImage(file);
    }

    @GetMapping(path = {"/get/image/info/{id}"})
    @PreAuthorize("hasAnyRole('organizer', 'participant')")
    public Image getImageDetails(@PathVariable("id") Long id) {
        return imageService.getImageDetails(id);
    }

}
