package devblackholemax.easychattingroom.config;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler
    @SendToUser("/queue/errors") // 发送错误信息到用户专属队列
    public String handleException(Exception ex) {
        return "错误: " + ex.getMessage();
    }
}