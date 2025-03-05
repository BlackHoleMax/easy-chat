package devblackholemax.easychattingroom.service;

import devblackholemax.easychattingroom.domain.EmojiImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmojiImageService {
    public void saveImage(MultipartFile file, String name) throws IOException, SQLException;
    public Optional<EmojiImage> getImageById(Long id);
    public List<EmojiImage> getAllImages();
}
