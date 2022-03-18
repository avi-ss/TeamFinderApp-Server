package es.albertolongo.teamfinderapp.exception.team;

public class MemberAlreadyInTeam extends RuntimeException{

    public MemberAlreadyInTeam(String message) {
        super(message);
    }
}
