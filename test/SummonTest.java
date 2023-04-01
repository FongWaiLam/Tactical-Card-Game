import static org.junit.Assert.assertTrue;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.HumanPlayer;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.OrderedCardLoader;

public class SummonTest {

	@Test
	public void testSummonHealth() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;

		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		Tile tile = Board.getBoard()[3][3];

		gameState.currentPlayer = HumanPlayer.getInstance();

		List<Card> player1 = OrderedCardLoader.spellCardMapping(OrderedCardLoader.getPlayer1Cards());
		Card card = player1.get(1);

		gameState.currentGame.getGameController().summonUnit(card, tile);
		HumanPlayer humanPlayer = HumanPlayer.getInstance();
		for (Unit unit : humanPlayer.getUnitsOnBoard()) {
			if (unit.getName().equals(card.getCardname())) {
				assertTrue(unit.getHealth() == card.getBigCard().getHealth());
			}
		}

	}

	@Test
	public void testSummonAttack() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;

		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		Tile tile = Board.getBoard()[3][3];

		gameState.currentPlayer = HumanPlayer.getInstance();

		List<Card> player1 = OrderedCardLoader.spellCardMapping(OrderedCardLoader.getPlayer1Cards());
		Card card = player1.get(1);

		gameState.currentGame.getGameController().summonUnit(card, tile);
		HumanPlayer humanPlayer = HumanPlayer.getInstance();
		for (Unit unit : humanPlayer.getUnitsOnBoard()) {
			if (unit.getName().equals(card.getCardname())) {
				assertTrue(unit.getAttack() == card.getBigCard().getAttack());
			}
		}

	}

}
