import org.junit.Test;

import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Game;
import structures.basic.HumanPlayer;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.OrderedCardLoader;
import utils.StaticConfFiles;

import static org.junit.Assert.assertTrue;

import java.util.List;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
/**
 * Test spell functions if heal, attack or kill target units
 * @author Ying Ting Liu
 */
public class SpellTest {
	
	GameState gameState = new GameState(); 
	Game game = new Game(null, gameState);
	Tile tile = Board.getBoard()[3][3];
	
	/*
	 * Truestrike Spell: Deal 2 damage to an enemy unit 
	 */
	@Test
	public void testTruestrikeSpell() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); 
		BasicCommands.altTell = altTell;		
		
		Unit unit = BasicObjectBuilders.loadUnit(StaticConfFiles.u_windshrike, 4, Unit.class);
		unit.setHealth(3);
		unit.setAttack(4);
		unit.setPositionByTile(tile);
		AIPlayer aiPlayer = AIPlayer.getInstance();
		aiPlayer.getUnitsOnBoard().add(unit);
		
		// the original health is 3
		assertTrue((unit.getHealth()==3)); 
		
		List<Card> player1 = OrderedCardLoader.spellCardMapping(OrderedCardLoader.getPlayer1Cards());
		Card card = player1.get(4); // Truestrike
		
		game.getGameController().castSpell(card, tile);
		// after spell the health is 1
		assertTrue((unit.getHealth()==1));		

	}
	
	
	/*
	 * Sundrop Elixir Spell: Add +5 health to a Unit. 
	 * This cannot take a unit over its starting health value.
	 */
	@Test
	public void testSundropElixirSpell() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); 
		BasicCommands.altTell = altTell;			

		Unit unit = new Unit();
		
		int health = 6;
		unit.setHealth(health);
		unit.setMaxHealth(health);
		unit.setAttack(4);
		unit.setPositionByTile(tile);
		HumanPlayer humanPlayer = HumanPlayer.getInstance();
		humanPlayer.getUnitsOnBoard().add(unit);
		
		// the original health is 6
		assertTrue((unit.getHealth()==6)); 
		
		// reduce the health
		unit.setHealth(4);
		assertTrue((unit.getHealth()==4)); 
		
		List<Card> player1 = OrderedCardLoader.spellCardMapping(OrderedCardLoader.getPlayer1Cards());
		Card card = player1.get(8); // Sundrop Elixir
		
		game.getGameController().castSpell(card, tile);
		// after spell the health is 6
		assertTrue((unit.getHealth()<=health));
		assertTrue((unit.getHealth()==6));
		
	}
	
	/*
	 * Entropic Decay Spell: Reduce a non-avatar unit to 0 health
	 */
	@Test
	public void testEntropicDecaySpell() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); 
		BasicCommands.altTell = altTell;		

		Unit unit = new Unit();
		
		int health = 3;
		unit.setHealth(health);
		unit.setMaxHealth(health);
		unit.setAttack(4);
		unit.setPositionByTile(tile);
		AIPlayer aiPlayer = AIPlayer.getInstance();
		aiPlayer.getUnitsOnBoard().add(unit);
		
		// the original health is 3
		assertTrue((unit.getHealth()==3)); 
		
		List<Card> player2 = OrderedCardLoader.spellCardMapping(OrderedCardLoader.getPlayer2Cards());
		Card card = player2.get(7); // Entropic Decay

		game.getGameController().castSpell(card, tile);

		assertTrue((unit.getHealth()==0));
		
	}


}
