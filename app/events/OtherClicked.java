package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Board;
import structures.basic.HumanPlayer;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// When unit is moving, no other action should be processed.
		if (gameState.unitMoving == true || gameState.currentPlayer != HumanPlayer.getInstance() || gameState.endGame == true) {
			return;
		}

		
		// FWL added on 19 Feb 17:25
		Board.resetWholeBoard(out);
		
		// Unselected card
		if (gameState.selectedHandPosition != -1 && gameState.onClickedCard != null) {

			BasicCommands.drawCard(out, gameState.onClickedCard, gameState.selectedHandPosition, 0);
			gameState.selectedHandPosition = -1;
			gameState.onClickedCard = null;
			
		}
		
		// Display: Refresh selected card
		gameState.currentGame.getCardController().showCardOnHand();		
		
		
	}

}


