package com.chekanova.imagetool.controller;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.MultithreadingStrategy;
import com.chekanova.imagetool.service.ImageService;
import com.chekanova.imagetool.validation.FileValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(value = "/process",
            produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> process(@RequestParam MultipartFile file,
                                          @RequestParam ImageProcessorType type,
                                          @RequestParam MultithreadingStrategy strategy,
                                          RedirectAttributes attributes) throws IOException, InterruptedException {
        FileValidationUtil.validateFile(file, attributes);
        ByteArrayOutputStream resultImage = imageService.reprocess(file, type, strategy);
        return ResponseEntity.ok().contentType(IMAGE_JPEG).body(resultImage.toByteArray());
    }

    @PostMapping(value = "/compare",
            produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> compare(@RequestParam MultipartFile file1,
                                          @RequestParam MultipartFile file2,
                                          RedirectAttributes attributes) throws IOException {
        return ResponseEntity.ok().contentType(IMAGE_PNG).body(imageService.compare(file1, file2).toByteArray());
    }
}