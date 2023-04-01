package events;


import com.fasterxml.jackson.databind.JsonNode;
import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.HumanPlayer;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * 
 * { 
 *   messageType = “cardClicked”
 *   position = <hand index position [1-6]>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class CardClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		// When unit is moving, no other action should be processed.
		if (gameState.unitMoving == true || gameState.currentPlayer != HumanPlayer.getInstance() || gameState.endGame == true) {
			return;
		}
		
		int handPosition = message.get("position").asInt();
		
		// Display: Refresh selected card
		gameState.currentGame.getCardController().showCardOnHand();
		// Display: Refresh the highlight
		Board.resetWholeBoard(out);

		// Get the Card
		Card onClickedCard = gameState.currentPlayer.getHand().getCard(handPosition);
		
		// TODO debug
		System.out.println("onClickedCard: " + onClickedCard);
		
		// Display: Card clicked highlight
		if (onClickedCard != null) {
			BasicCommands.drawCard(out, onClickedCard, handPosition, 1);
			try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
			gameState.currentGame.getCardController().onClickedCard(onClickedCard, handPosition);
		}
	}
	

	

}
