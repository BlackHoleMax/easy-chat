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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// 简单的限流过滤器
class RateLimitFilter extends OncePerRequestFilter {
    private static final int MAX_REQUESTS = 10; // 每分钟最大请求数
    private static final long TIME_WINDOW = 60 * 1000; // 时间窗口（1分钟）
    private final ConcurrentHashMap<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        RequestCounter counter = requestCounters.computeIfAbsent(clientIp, k -> new RequestCounter());

        long currentTime = System.currentTimeMillis();
        if (currentTime - counter.lastResetTime > TIME_WINDOW) {
            counter.reset();
        }

        if (counter.count.incrementAndGet() > MAX_REQUESTS) {
            response.setStatus(429);
            response.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        filterChain.doFilter(new ContentCachingRequestWrapper(request), response);
    }

    private static class RequestCounter {
        final AtomicInteger count = new AtomicInteger(0);
        long lastResetTime = System.currentTimeMillis();

        void reset() {
            count.set(0);
            lastResetTime = System.currentTimeMillis();
        }
    }
}

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
        try {
            emojiImageService.saveImage(file, name);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading image");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        // 输入验证
        if (id == null) {
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