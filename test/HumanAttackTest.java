import static org.junit.Assert.assertEquals;

import org.junit.Test;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import controllers.ActionController;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Game;
import structures.basic.HumanPlayer;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class HumanAttackTest {

	@Test
	public void checkHumanAttack() {
		
		// First override the alt tell variable so we can issue commands without a running front-end
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		
		// As we are not starting the front-end, we have no GameActor, so lets manually create
		// the components we want to test
		GameState gameState = new GameState(); // create state storage
		gameState.currentGame = new Game(null, gameState);
		ActorRef out=null;
		ActionController actionController=new ActionController(out, gameState);
		
		gameState.currentPlayer = AIPlayer.getInstance();
		// AI: c_blaze_hound
		Card aiCard = BasicObjectBuilders.loadCard(StaticConfFiles.c_blaze_hound, 23, Card.class);
		Unit aiUnit = BasicObjectBuilders.loadUnit(aiCard.cardStaticConfFile(), 23, Unit.class);
		Tile aiTile = Board.getBoard()[2][5];
		aiUnit.setPositionByTile(aiTile);
		// set name
		String name = aiCard.getCardname();
		aiUnit.setName(name);
		aiUnit.setAbilities();
		
		// set attack
		int humanAttack = aiCard.getBigCard().getAttack();
		aiUnit.setAttack(humanAttack);
		// set health
		int health = aiCard.getBigCard().getHealth();

		aiUnit.setHealth(health);
		aiUnit.setMaxHealth(health);


		// Add an unitOnBoard
		gameState.currentPlayer.getUnitsOnBoard().add(aiUnit);
		
		gameState.currentPlayer = HumanPlayer.getInstance();
		
		// Human: c_azurite_lion
		Card humanCard = BasicObjectBuilders.loadCard(StaticConfFiles.c_azurite_lion, 7, Card.class);
		Unit humanUnit = BasicObjectBuilders.loadUnit(humanCard.cardStaticConfFile(), 23, Unit.class);
		Tile humanTile = Board.getBoard()[2][6];
		humanUnit.setPositionByTile(humanTile);
		// set name
		name = humanCard.getCardname();
		humanUnit.setName(name);
		humanUnit.setAbilities();
		
		// set attack
		humanAttack = humanCard.getBigCard().getAttack();
		humanUnit.setAttack(humanAttack);
		// set health
		health = humanCard.getBigCard().getHealth();

		humanUnit.setHealth(health);
		humanUnit.setMaxHealth(health);
		
		// Moved, only can attack
		humanUnit.setAttackChance(1);
		humanUnit.setMoveChance(0);

		// Add an unitOnBoard
		gameState.currentPlayer.getUnitsOnBoard().add(humanUnit);
		
		
		actionController.selectEnemyToAttack(humanUnit, aiUnit);
		
		//Checks if the unit has been attacked with specified attack value
		assertEquals(aiUnit.getMaxHealth()-humanAttack, aiUnit.getHealth()); 
		
	}
}
