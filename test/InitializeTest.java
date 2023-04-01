import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.HumanPlayer;
import structures.basic.Card;

/**
 * In these test, the initialisation process of the game would be tested. This includes the currentGame creation,
 * 3 cards in human player's hand, assignment of players to avatars and mana gain for human player.
 * @author Fong Wai Lam
 *
 */
public class InitializeTest {


	@Test
	public void checkGameInitialization() {
		

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		assertFalse(gameState.currentGame != null && gameState.currentPlayer != null && gameState.waitingPlayer != null);
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		assertTrue(gameState.currentGame != null && gameState.currentPlayer != null && gameState.waitingPlayer != null);
		
	}
	
	@Test
	public void checkHumanPlayerHandCard() {
		

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		Card[] HandOfCards = HumanPlayer.getInstance().getHand().getHandOfCards();
		
		int countOfCards = 0;
		
		for (Card card : HandOfCards) {
			if (card != null) {
				countOfCards++;
			}
		}

		System.out.println("Count of Cards: " + countOfCards);
		assertTrue(countOfCards == 3);
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		countOfCards = 0;
		
		for (Card card : HandOfCards) {
			if (card != null) {
				countOfCards++;
			}
		}

		assertTrue(countOfCards == 3);
	}
	
	@Test
	public void checkAvatarPlayer() {
		

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		assertTrue(Avatar.avatar1.getPlayer() != null && Avatar.avatar2.getPlayer() != null);

	}
	
	@Test
	public void checkMana() {
		

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		assertTrue(HumanPlayer.getInstance().getMana() == 2);

	}
	
	
}
