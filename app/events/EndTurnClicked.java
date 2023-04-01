package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Game;
import structures.basic.HumanPlayer;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// When unit is moving, no other action should be processed.
		if (gameState.unitMoving == true || gameState.currentPlayer != HumanPlayer.getInstance() || gameState.endGame == true) {
			return;
		}

		Game game = gameState.currentGame;

		game.getPlayerController().endTurn();

	}

}
