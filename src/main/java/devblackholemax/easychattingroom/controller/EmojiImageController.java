package devblackholemax.easychattingroom.controller;

import devblackholemax.easychattingroom.domain.EmojiImage;
import devblackholemax.easychattingroom.service.impl.EmojiImageServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/emoji-images")
public class EmojiImageController {
    @Resource
    private EmojiImageServiceImpl emojiImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        // 输入验证
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required");
        }
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }
        // 文件类型和大小验证
        if (!Objects.equals(file.getContentType(), MediaType.IMAGE_PNG_VALUE) && !file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)) {
            return ResponseEntity.badRequest().body("Only PNG and JPEG images are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            return ResponseEntity.badRequest().body("File size exceeds limit (5MB)");
        }
        try {
            emojiImageService.saveImage(file, name);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            // 记录异常信息
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading image");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        // 输入验证
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<EmojiImage> optionalEmojiImage = emojiImageService.getImageById(id);
        if (optionalEmojiImage.isPresent()) {
            EmojiImage emojiImage = optionalEmojiImage.get();
            byte[] imageBytes = emojiImage.getImageData();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<EmojiImage> getAllImages() {
        return emojiImageService.getAllImages();
    }
}