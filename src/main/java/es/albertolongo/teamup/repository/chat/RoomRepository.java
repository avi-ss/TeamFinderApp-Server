package es.albertolongo.teamup.repository.chat;

import es.albertolongo.teamup.model.entity.chat.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    Optional<Room> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
