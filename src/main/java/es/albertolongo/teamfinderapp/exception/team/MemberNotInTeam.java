package es.albertolongo.teamfinderapp.exception.team;

public class MemberNotInTeam extends RuntimeException{

    public MemberNotInTeam(String message) {
        super(message);
    }
}
