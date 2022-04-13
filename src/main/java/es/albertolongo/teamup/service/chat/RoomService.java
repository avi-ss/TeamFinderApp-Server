package es.albertolongo.teamup.service.chat;

import es.albertolongo.teamup.model.entity.chat.Room;
import es.albertolongo.teamup.repository.chat.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        return roomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(Room::getChatId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    var chatId =
                            String.format("%s_%s", senderId, recipientId);

                    Room senderRecipient = new Room(chatId, senderId, recipientId);
                    Room recipientSender = new Room(chatId, recipientId, senderId);

                    roomRepository.save(senderRecipient);
                    roomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}