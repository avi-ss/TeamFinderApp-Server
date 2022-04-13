package es.albertolongo.teamup.exception.team;

public class MemberNotInTeam extends RuntimeException{

    public MemberNotInTeam(String message) {
        super(message);
    }
}
