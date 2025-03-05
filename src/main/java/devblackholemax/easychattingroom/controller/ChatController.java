package devblackholemax.easychattingroom.controller;

import devblackholemax.easychattingroom.domain.ChatMessage;
import devblackholemax.easychattingroom.domain.Result;
import devblackholemax.easychattingroom.service.ChatMessageService;
import devblackholemax.easychattingroom.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class ChatController {
    @Resource
    ChatMessageService chatMessageService;

    @Resource
    UserService userService;

    @GetMapping("/index")
    public String index() {
        return "static/index.html";
    }

    @GetMapping("/register")
    public String register() {
        return "static/register.html";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        if (userService.getUserByName(message.getSender()) != null) {
            chatMessageService.createMessage(message);
            return message;
        }
        return null;
    }

//    @MessageMapping("/chat/img")
//    @SendTo("/topic/messages")
//    public EmojiImageMessage sendImage(@Validated EmojiImageMessage emojiImageMessage) {
//        if (userService.getUserByName(emojiImageMessage.getSender()) != null) {
//            return emojiImageMessage;
//        }
//        return null;
//    }

    @GetMapping("/web/messages")
    public List<ChatMessage> getMessages() {
        return chatMessageService.getAllMessages();
    }

    @GetMapping("/web/messages/{id}")
    public List<ChatMessage> findMessagesById(@PathVariable Long id) {
        return chatMessageService.findMessagesById(id);
    }

    @GetMapping("/web/messages/clear")
    public Result clearMessage() {
        try {
            chatMessageService.clearMessages();
            return Result.success("聊天记录已清空");
        } catch (Exception e) {
            return Result.error("清空聊天记录失败");
        }
    }
}