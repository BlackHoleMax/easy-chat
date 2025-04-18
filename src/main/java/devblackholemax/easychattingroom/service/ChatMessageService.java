package devblackholemax.easychattingroom.service;

import devblackholemax.easychattingroom.domain.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    public void createMessage(ChatMessage message);

    public List<ChatMessage> getAllMessages();

    public void clearMessages();
}
