package com.virtualmarathon.core;

import com.virtualmarathon.core.controller.ImageController;
import com.virtualmarathon.core.dao.ImageDao;
import com.virtualmarathon.core.dao.RoleDao;
import com.virtualmarathon.core.entity.Image;
import com.virtualmarathon.core.entity.User;
import com.virtualmarathon.core.service.ImageService;
import com.virtualmarathon.core.service.RoleService;
import com.virtualmarathon.core.util.ImageUtility;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class ImageServiceTest {

    @MockBean
    private ImageDao imageDao;

    @InjectMocks
    private ImageService imageService;


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    Image image = new Image();
    private static final String urlPath = "/upload/image";

    @BeforeEach
    public void setup(){
        image.setId(1L);
        image.setName("RoadMap.jpg");
        image.setType("image/jpeg");
        image.setImage(new byte[10100111]);
        mockMvc = webAppContextSetup(webApplicationContext).build();
      //  imageService = Mockito.mock(ImageService.class);
    }

    @AfterEach
    public void tearDown(){
        image=null;
    }

    @Test
    public void successfullySetImage() throws Exception {
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/Images/PaytmVoucher.png");
        final MockMultipartFile image = new MockMultipartFile("file", "/Images/PaytmVoucher.png", "image/png", inputStream);

        try {

            doThrow().when(imageService).uplaodImage(image);
            final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(urlPath).file(image))
                    .andExpect(status().isOk())
                    .andReturn();

            Mockito.verify(imageService).uplaodImage(image);
        } catch (Exception e) {
            System.out.println("not found");
        }

    }

    @Test
    void getImageDetailsofparticularId(){
        // Setup our mock repository
        doReturn(Optional.of(image)).when(imageDao).findById(1L);

        // Execute the service call
        Optional<Image> images = Optional.ofNullable(imageService.getImageDetails(1L));

        // Assert the response
        Assertions.assertTrue(images.isPresent(), "image was found");
        Assertions.assertSame(images.get().getName(), image.getName(), "The image name returned was the same as the mock image name");
        Assertions.assertSame(images.get().getType(),image.getType(),"Check Type Type");
    }
}
