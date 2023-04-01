import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import play.libs.Json;
import events.Initalize;
import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import controllers.ActionController;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Position;
import structures.basic.Tile;
import structures.basic.Unit;

public class HumanMoveTest {
	@Test
	public void checkSelectTileToMove() {		

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); 
		BasicCommands.altTell = altTell; 
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);

		ActionController actionController=new ActionController(null, gameState);
		
		Unit selectedUnit=new Unit();
		selectedUnit.setMoveChance(1);
		Tile tile = Board.getBoard()[3][2];
		selectedUnit.setPositionByTile(tile);
		int arrivedTileX=3; 
		int arrivedTileY=3;//create specified position
		
		gameState.currentGame.getActionController().selectTileToMove(selectedUnit, arrivedTileX, arrivedTileY);
		//Checks if the unit has moved to the specified position
		
		assertEquals(arrivedTileX,selectedUnit.getTile().getTilex()); 
		assertEquals(arrivedTileY,selectedUnit.getTile().getTiley());
		
	}
	
}
