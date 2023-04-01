import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;

import static org.junit.Assert.assertTrue;

import structures.GameState;
import structures.basic.aiLogic.AvailableSteps;
import structures.basic.aiLogic.Direction;

public class AvailableStepTest {

	@Test
	public void testClear() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		AvailableSteps availableSteps = new AvailableSteps();
		availableSteps.clear();
		
		assertTrue(availableSteps.getMoves().isEmpty());
	}
	
	@Test
	public void testPair() {
		
		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell();
		BasicCommands.altTell = altTell;
		
		GameState gameState = new GameState();
		Initalize initalizeProcessor =  new Initalize();
		
		ObjectNode eventMessage = Json.newObject();
		initalizeProcessor.processEvent(null, gameState, eventMessage);
		
		AvailableSteps availableSteps = new AvailableSteps();
		Pair<Integer, Direction> result = availableSteps.distance(0, 0, 2, 0);
        assertTrue((Pair.of(2, Direction.RIGHT)).equals(result));		

	}

}
