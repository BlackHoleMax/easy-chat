package devblackholemax.easychattingroom.service;

import devblackholemax.easychattingroom.domain.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ImageService {
    public void saveImage(MultipartFile file, String name) throws IOException, SQLException;
    public Optional<Image> getImageById(Long id);
    public List<Image> getAllImages();
}
