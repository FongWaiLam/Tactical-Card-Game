import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Avatar;
import structures.basic.Board;
import structures.basic.HumanPlayer;
import structures.basic.Tile;

/**
 * This is a JUnit test for units' game actions. 
 * The actions include move, attack and "move and attack".
 * 
 * @author Fong Wai Lam
 */
public class ActionTest {
	
	@Test
	public void checkMoveAndAttack() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		
		ObjectNode eventMessage = Json.newObject();
		Initalize initalizeProcessor =  new Initalize();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		

		gameState.currentPlayer = AIPlayer.getInstance();
		Tile aiAvatarTile = Board.getBoard()[2][4];
		Avatar.avatar2.setPositionByTile(aiAvatarTile);
		

		gameState.currentPlayer = HumanPlayer.getInstance();
		gameState.onClickedCard = null;
		gameState.selectedUnit = Avatar.avatar1;
		gameState.selectedUnit.setAttackChance(1);
		gameState.selectedUnit.setMoveChance(1);
		
		Tile humanAvatarTile = Board.getBoard()[2][1];
		Avatar.avatar1.setPositionByTile(humanAvatarTile);


		gameState.currentGame.getActionController().selectEnemyToAttack(Avatar.avatar1, Avatar.avatar2);

		assertTrue(Avatar.avatar2.getHealth() < 20);
		
	}
	
	
}
