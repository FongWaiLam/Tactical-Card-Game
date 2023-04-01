import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.EndTurnClicked;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.HumanPlayer;
import structures.basic.Player;

/**
 * Test the round ends when the EndTurn button is clicked, the current player
 * switches, and the mana is cleared.
 * 
 * @author JingYi Xiang
 */

public class EndTurnTest {

	private HumanPlayer humanPlayer = HumanPlayer.getInstance();
	private AIPlayer aiPlayer = AIPlayer.getInstance();

	@Test
	public void checkPlayerSwitch() throws InterruptedException {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState(); // create state storage
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		// Get the current player before the event is processed
		Player currentPlayerBefore = gameState.currentPlayer;

		if (currentPlayerBefore == humanPlayer) {

			// click the EndTurn
			EndTurnClicked endTurnClicked = new EndTurnClicked();
			ObjectNode eventMessage2 = Json.newObject();
			endTurnClicked.processEvent(null, gameState, eventMessage2);

			// Get the current player after the event is processed
			Player currentPlayerAfter = gameState.currentPlayer;

			assertTrue(currentPlayerAfter == aiPlayer);
			assertTrue(gameState.waitingPlayer == humanPlayer);
			assertFalse(currentPlayerAfter.equals(currentPlayerBefore));

		}
	}

	@Test
	public void checkCleanMana() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;

		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		if (gameState.currentPlayer.getMana() != 0) {

			// click the EndTurn
			EndTurnClicked endTurnClicked = new EndTurnClicked();
			ObjectNode eventMessage2 = Json.newObject();
			endTurnClicked.processEvent(null, gameState, eventMessage2);

			// Check the waitingPlayer's mana is cleared
			assertEquals(0, gameState.waitingPlayer.getMana());

		}
	}

}
