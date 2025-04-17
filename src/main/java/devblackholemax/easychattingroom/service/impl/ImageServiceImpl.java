package devblackholemax.easychattingroom.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import devblackholemax.easychattingroom.dao.ImageRepository;
import devblackholemax.easychattingroom.domain.Image;
import devblackholemax.easychattingroom.service.ImageService;
import jakarta.annotation.Resource;

@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    ImageRepository imageRepository;

    @Override
    public void saveImage(MultipartFile file, String name) throws IOException {
        byte[] imageData = file.getBytes();

        Optional<Image> existingImage = imageRepository.findByNameAndImageData(name, imageData);
        if (existingImage.isPresent()) {
            throw new IllegalArgumentException("相同的文件已经存在");
        }

        Image image = new Image();
        image.setName(name);
        image.setImageData(imageData);
        imageRepository.save(image);
    }

    @Override
    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public List<Image> getAllImages() {
        Pageable pageable = PageRequest.of(0, 30);
        Page<Image> emojiImagePage = imageRepository.findAll(pageable);
        return emojiImagePage.getContent();
    }
}