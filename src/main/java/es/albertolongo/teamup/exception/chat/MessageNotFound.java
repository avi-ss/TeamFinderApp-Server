package es.albertolongo.teamup.exception.chat;

public class MessageNotFound extends RuntimeException{

    public MessageNotFound(String message) {
        super(message);
    }
}