package es.albertolongo.teamup.exception.team;

public class MemberAlreadyInTeam extends RuntimeException{

    public MemberAlreadyInTeam(String message) {
        super(message);
    }
}
