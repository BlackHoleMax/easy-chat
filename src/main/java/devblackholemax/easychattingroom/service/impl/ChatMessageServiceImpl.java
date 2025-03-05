package devblackholemax.easychattingroom.service.impl;

import devblackholemax.easychattingroom.dao.ChatMessageRepository;
import devblackholemax.easychattingroom.domain.ChatMessage;
import devblackholemax.easychattingroom.service.ChatMessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Resource
    ChatMessageRepository chatMessageRepository;

    @Override
    public void createMessage(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findTop30ByOrderByIdDesc();
    }

    @Override
    public List<ChatMessage> findMessagesById(Long id) {
        return chatMessageRepository.findAllById(Collections.singleton(id));
    }

    @Override
    public void clearMessages() {
        chatMessageRepository.deleteAll();
    }
}
