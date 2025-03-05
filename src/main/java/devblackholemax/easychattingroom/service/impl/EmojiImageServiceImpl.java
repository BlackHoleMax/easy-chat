package devblackholemax.easychattingroom.service.impl;

import devblackholemax.easychattingroom.dao.EmojiImageRepository;
import devblackholemax.easychattingroom.domain.EmojiImage;
import devblackholemax.easychattingroom.service.EmojiImageService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class EmojiImageServiceImpl implements EmojiImageService {
    @Resource
    EmojiImageRepository emojiImageRepository;

    @Override
    public void saveImage(MultipartFile file, String name) throws IOException {
        byte[] imageBytes = file.getBytes();
        EmojiImage emojiImage = new EmojiImage();
        emojiImage.setName(name);
        emojiImage.setImageData(imageBytes);
        emojiImageRepository.save(emojiImage);
    }

    @Override
    public Optional<EmojiImage> getImageById(Long id) {
        return emojiImageRepository.findById(id);
    }


    @Override
    public List<EmojiImage> getAllImages() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<EmojiImage> emojiImagePage = emojiImageRepository.findAll(pageable);
        return emojiImagePage.getContent();
    }
}
