package ca.sheridancollege.restfulhousekeeping.controllers;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import ca.sheridancollege.restfulhousekeeping.models.SendChatMessageRequest;
import ca.sheridancollege.restfulhousekeeping.services.ChatService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(
            SendChatMessageRequest request,
            Principal principal) {
        chatService.saveAndBroadcastMessage(request, principal);
    }
}
