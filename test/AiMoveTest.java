import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;

import static org.junit.Assert.assertTrue;

import structures.GameState;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.aiLogic.Direction;
import structures.basic.aiLogic.Move;

public class AiMoveTest {

	@Test
	public void testSetDirection() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		Unit unit = new Unit();
		List<Tile> tiles = new ArrayList<>();
		tiles.add(new Tile());
		tiles.add(new Tile());
		Move move = new Move(unit, tiles);
		move.setDirection(Direction.DOWN);
		assertTrue(move.getDirection() == Direction.DOWN);
		
	}

}
