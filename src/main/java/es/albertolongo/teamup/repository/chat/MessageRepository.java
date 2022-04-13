package es.albertolongo.teamup.repository.chat;

import es.albertolongo.teamup.model.entity.chat.Message;
import es.albertolongo.teamup.model.enums.MessageStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByChatId(String chatId);
    long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);
}