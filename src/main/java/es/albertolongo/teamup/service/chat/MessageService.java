package es.albertolongo.teamup.service.chat;

import es.albertolongo.teamup.exception.chat.MessageNotFound;
import es.albertolongo.teamup.model.entity.chat.Message;
import es.albertolongo.teamup.model.enums.MessageStatus;
import es.albertolongo.teamup.repository.chat.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    RoomService roomService;

    public Message save(Message chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(String senderId, String recipientId) {
        return messageRepository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<Message> findChatMessages(String senderId, String recipientId) {
        var chatId = roomService.getChatId(senderId, recipientId, false);

        var messages =
                chatId.map(cId -> messageRepository.findByChatId(cId)).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(messages, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public Message findById(Long id) {
        return messageRepository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return messageRepository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new MessageNotFound("Can't find message (" + id + ")"));
    }

    public void updateStatuses(List<Message> messages, MessageStatus status) {

        messages = messages.stream().map(message -> {
            message.setStatus(status);
            return message;
        }).collect(Collectors.toList());

        messageRepository.saveAll(messages);
    }
}
