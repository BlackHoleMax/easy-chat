package devblackholemax.easychattingroom.controller;

import devblackholemax.easychattingroom.domain.Image;
import devblackholemax.easychattingroom.service.impl.ImageServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;

@RestController
@RequestMapping("/emoji-images")
public class ImageController {
    @Resource
    private ImageServiceImpl emojiImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required");
        }
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }
        if (!Objects.equals(file.getContentType(), MediaType.IMAGE_PNG_VALUE) && !Objects.equals(file.getContentType(), MediaType.IMAGE_JPEG_VALUE) && !Objects.equals(file.getContentType(), MediaType.IMAGE_GIF_VALUE)) {
            return ResponseEntity.badRequest().body("Only PNG,JPEG and GIF images are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("File size exceeds limit (5MB)");
        }
        try {
            emojiImageService.saveImage(file, name);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading image");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Image> optionalEmojiImage = emojiImageService.getImageById(id);
        if (optionalEmojiImage.isPresent()) {
            Image image = optionalEmojiImage.get();
            byte[] imageBytes = image.getImageData();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/compressed/{id}")
    public ResponseEntity<byte[]> getCompressedImage(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Image> optionalEmojiImage = emojiImageService.getImageById(id);
        if (optionalEmojiImage.isPresent()) {
            Image image = optionalEmojiImage.get();
            try {
                BufferedImage bufferedImage = Thumbnails.of(new ByteArrayInputStream(image.getImageData()))
                        .size(100, 100)
                        .outputFormat("jpg")
                        .asBufferedImage();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos);
                byte[] compressedImage = baos.toByteArray();

                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(compressedImage);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(null);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Image> getAllImages() {
        return emojiImageService.getAllImages();
    }
}