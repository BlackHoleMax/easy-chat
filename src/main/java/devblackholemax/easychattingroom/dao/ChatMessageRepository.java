package devblackholemax.easychattingroom.dao;

import devblackholemax.easychattingroom.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop30ByOrderByIdDesc();
}
