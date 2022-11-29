package com.virtualmarathon.core.service;

import com.virtualmarathon.core.dao.ImageDao;
import com.virtualmarathon.core.entity.Image;
import com.virtualmarathon.core.response.ImageUploadResponse;
import com.virtualmarathon.core.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    public ResponseEntity<ImageUploadResponse> uplaodImage(MultipartFile file) throws Exception {
        Image image = Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(ImageUtility.compressImage(file.getBytes())).build();
        Long imageId = imageDao.save(image).getId();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename(), imageId));
    }

    public Image getImageDetails(Long id) {
        final Optional<Image> dbImage = imageDao.findById(id);
        return Image.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .image(ImageUtility.decompressImage(dbImage.get().getImage())).build();
    }


}
