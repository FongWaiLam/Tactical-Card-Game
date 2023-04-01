import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import controllers.GameResultHandler;
import events.Initalize;
import play.libs.Json;
import structures.GameState;

/**
 * Tests for two game-over situations, (1) Avatar's health drops to zero (2) No
 * remaining cards in the deck.
 * 
 * @author JingYi Xiang
 *
 */

public class EndGameTest {

	@Test
	public void checkAvatarHealth() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;

		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		GameResultHandler gameResult = new GameResultHandler();
		gameResult.checkResult(gameState, null);

		if (gameState.currentPlayer.getHealth() == 0 || gameState.waitingPlayer.getHealth() == 0) {
			assertTrue(gameState.endGame);
		}

	}

	@Test
	public void checkDrawCard() {
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;

		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		GameResultHandler gameResult = new GameResultHandler();
		gameResult.checkResultWhenDrawCard(gameState, null);

		if (gameState.currentPlayer.getDeck().size() == 0 || gameState.waitingPlayer.deckRemainingCard == 0) {
			assertTrue(gameState.endGame);
		}
	}

}
