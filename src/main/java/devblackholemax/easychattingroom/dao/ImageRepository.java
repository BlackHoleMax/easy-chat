package devblackholemax.easychattingroom.dao;

import devblackholemax.easychattingroom.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByNameAndImageData(String name, byte[] imageData);
}