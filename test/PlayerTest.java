import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.HumanPlayer;

public class PlayerTest {

	@Test
	public void testSingleton() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;

		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		HumanPlayer humanPlayer = HumanPlayer.getInstance();
		gameState.currentPlayer = HumanPlayer.getInstance();

		assertTrue(gameState.currentPlayer.equals(humanPlayer));
	}

}
