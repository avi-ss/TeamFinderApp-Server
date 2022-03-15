package es.albertolongo.teamfinderapp.app.rest;

import es.albertolongo.teamfinderapp.api.PlayerApi;
import es.albertolongo.teamfinderapp.model.PlayerBody;
import es.albertolongo.teamfinderapp.model.PlayerBodyId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PlayerRestController implements PlayerApi {

    @Override
    public ResponseEntity<Void> playerPlayerIdDelete(Integer playerId) {
        return PlayerApi.super.playerPlayerIdDelete(playerId);
    }

    @Override
    public ResponseEntity<PlayerBody> playerPlayerIdGet(Integer playerId) {
        return PlayerApi.super.playerPlayerIdGet(playerId);
    }

    @Override
    public ResponseEntity<Void> playerPlayerIdPut(Integer playerId, PlayerBody playerBody) {
        return PlayerApi.super.playerPlayerIdPut(playerId, playerBody);
    }

    @Override
    public ResponseEntity<Integer> playerPost(PlayerBodyId playerBodyId) {
        return PlayerApi.super.playerPost(playerBodyId);
    }
}
