package devblackholemax.easychattingroom.dao;

import devblackholemax.easychattingroom.domain.EmojiImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmojiImageRepository extends JpaRepository<EmojiImage, Long> {
    Optional<EmojiImage> findByNameAndImageData(String name, byte[] imageData);
}