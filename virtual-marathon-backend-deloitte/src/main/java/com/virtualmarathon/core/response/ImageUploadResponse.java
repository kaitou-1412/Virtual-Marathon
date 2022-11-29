package com.virtualmarathon.core.response;

public class ImageUploadResponse {

    public String message;
    public Long imageId;

    public ImageUploadResponse(String message, Long imageId) {
        this.message = message;
        this.imageId = imageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

}
