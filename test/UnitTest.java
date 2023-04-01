import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.HumanPlayer;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.ability.AbilityType;
import utils.UnitAbilityMapper;
import structures.basic.Card;
import structures.basic.Game;

/**
 * This is a JUnit test for units. These test will check the update and
 * correctness of variables of units, including name, health, attack, maxHealth
 * and abilities.
 * @author Fong Wai Lam
 *
 */
public class UnitTest {

	@Test
	public void checkUnitName() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;

		GameState gameState = new GameState();
		Initalize initalizeProcessor = new Initalize();

		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		Card humanCard = HumanPlayer.getInstance().getDeck().get(0);
		Tile summonTile = Board.getBoard()[2][2];

		gameState.currentGame.getGameController().summonUnit(humanCard, summonTile);

		Unit unit = HumanPlayer.getInstance().getUnitsOnBoard().get(1);

		assertTrue(unit.getName().equals(humanCard.getCardname()));

	}
	
	@Test
	public void checkUnitAttack() {
		

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		Card humanCard = HumanPlayer.getInstance().getDeck().get(0);
		Tile summonTile = Board.getBoard()[2][2];
		
		gameState.currentGame.getGameController().summonUnit(humanCard, summonTile);
		
		Unit unit = HumanPlayer.getInstance().getUnitsOnBoard().get(1);
		
		assertTrue(unit.getAttack() == humanCard.getBigCard().getAttack());
		
	}
	
	@Test
	public void checkUnitMaxHealth() {
		

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		Card humanCard = HumanPlayer.getInstance().getDeck().get(0);
		Tile summonTile = Board.getBoard()[2][2];
		
		gameState.currentGame.getGameController().summonUnit(humanCard, summonTile);
		
		Unit unit = HumanPlayer.getInstance().getUnitsOnBoard().get(1);
		
		assertTrue(unit.getMaxHealth() == humanCard.getBigCard().getHealth());
		
	}
	
}
