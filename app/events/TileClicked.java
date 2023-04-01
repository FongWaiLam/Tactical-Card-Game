package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Board;
import structures.basic.HumanPlayer;
import structures.basic.Tile;
import structures.basic.ability.SpellThiefAbility;

/**
 * Indicates that the user has clicked an object on the game canvas, in this
 * case a tile. The event returns the x (horizontal) and y (vertical) indices of
 * the tile that was clicked. Tile indices start at 1.
 * 
 * { messageType = “tileClicked” tilex = <x index of the tile> tiley = <y index
 * of the tile> }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor {

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		// When unit is moving, no other action should be processed.
		if (gameState.unitMoving == true || gameState.currentPlayer != HumanPlayer.getInstance() || gameState.endGame == true) {
			return;
		}
//
//		Unit selectedUnit;
//		Unit enemyUnit;

		int tilex = message.get("tilex").asInt();
		int tiley = message.get("tiley").asInt();

		
		// Tile clicked
		Tile tileClicked = Board.getBoard()[tiley][tilex];

// Scenario (1) Play Card
		// Scenario (1) Play Card
		
		// xhc feb 25
		if(gameState.onClickedCard != null) {			
			if (!gameState.currentGame.getGameController().playCard(tileClicked)) {
				BasicCommands.addPlayer1Notification(out, "Play Card failed, card unselected.", 2);
			} else { 				
				// for Human Player: Pureblade Enforcer creature - YingTing Mar 3rd
				// if spell return true, check spell thief 								 
//				gameState.currentGame.getGameController().checkSpellThief();
				SpellThiefAbility spellThiefAbility = new SpellThiefAbility();
				spellThiefAbility.perform(out, gameState);
			}
			
			// Successfully played the card, reset onClickedCard to null
			gameState.onClickedCard = null;
			gameState.selectedHandPosition = -1;
			// Display: Refresh selected card
			gameState.currentGame.getCardController().showCardOnHand();
			// A new unit is clicked, refresh the highlight
			Board.resetWholeBoard(out);


		} else {
			
			gameState.currentGame.getActionController().unitAction(tilex, tiley);

		}

	}
}
