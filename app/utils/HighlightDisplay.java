package utils;

import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.basic.Card;
import structures.basic.Tile;

public class HighlightDisplay {

	// Feel Free to move to another package you found suitable
	// FWL 22 Feb 02:38
	// Mode 0: No Highlight; Mode 1: White Highlight; Mode 2: Red Highlight
	public static void drawHighlight(ActorRef out, Tile tile, int mode) {
		BasicCommands.drawTile(out, tile, mode);
		try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	// FWL 22 Feb 23:50
	public static void drawSetHighlight(ActorRef out, Set<Tile> tileSet, int mode) {
		
		// White Highlight Valid Tiles for Move
		for (Tile tile: tileSet) {
			drawHighlight(out, tile, mode);
		}
	}
	
	
	
//	int spellHealth;
//	int spellAttack;
//	boolean spellEnemy = false;
//	boolean spellKillUnit = false;
//	boolean spellAvatar = false;
//	boolean heathCap = false;
	
	// FWL 23 Feb 18:19
	public static void drawSpellHighlight(ActorRef out, Set<Tile> tileSet, Card card) {

		// TODO - Switch to spell attributes
				int cardID = card.getId();
		// Highlight according to different spells
		switch (cardID) {
			// Player 1 - Truestrike - Deal 2 damage to an enemy unit
			// Scope: enemyTiles
		case 4 :
		case 14 :		
			// Player 2 - Entropic Decay - Reduce a non-avatar unit to 0 health
			// All enemy units, except enemy's avatar(index = 0)			
		case 27 :
		case 37 :
			// Red Highlight Valid Tiles for Attack
			HighlightDisplay.drawSetHighlight(out, tileSet, 2);
			break;
			
			// Player 1 - Sundrop Elixir - Add +5 health to a Unit. Not take a unit over its starting health
			// Scope: friendlyTiles
		case 8 :
		case 18 :
			
			// Player 2 - Staff of Y’Kir - Add +2 attack to your avatar
			// Scope: Current player's avatar
		case 22 :
		case 32 :
			// White Highlight Valid Tiles for adding attack value
			HighlightDisplay.drawSetHighlight(out, tileSet, 1);
			break;
		}

	}
	

//	// FWL 19 Feb 14:34
//	// Story# 24
//	// PlayCad (When it is a spell Card)
//	public static void targetHighlight(ActorRef out, GameState gameState, int cardClickedID) {
//		// Get UnitsOnBoard for validation
//		ArrayList<Unit> friendlyUnits = gameState.currentPlayer.getUnitsOnBoard();
//		ArrayList<Unit> enemyUnits = gameState.waitingPlayer.getUnitsOnBoard();
//		int tilex;
//		int tiley;
//		
//		// Highlight according to different spells
//		switch (cardClickedID) {
//		// Player 1 - Truestrike - Deal 2 damage to an enemy unit
//		case 4 :
//		case 14 :
//			for (Unit unit: enemyUnits) {
//				tilex = unit.getPosition().getTilex();
//				tiley = unit.getPosition().getTiley();
//	
//				// Red Highlight Valid Tiles for Attack
//					drawHighlight(out, Board.getBoard()[tiley][tilex], 2);
//			}
//			break;
//		// Player 1 - Sundrop Elixir - Add +5 health to a Unit. Not take a unit over its starting health
//		case 8 :
//		case 18 :
//			// All friendly units
//			for (Unit unit: friendlyUnits) {
//				tilex = unit.getPosition().getTilex();
//				tiley = unit.getPosition().getTiley();
//	
//				// White Highlight Valid Tiles for Heal
//					drawHighlight(out, Board.getBoard()[tiley][tilex], 1);
//			}
//			break;
//		// Player 2 - Staff of Y’Kir - Add +2 attack to your avatar
//		case 22 :
//		case 32 :
//			// Current player's avatar
//				tilex = friendlyUnits.get(0).getPosition().getTilex();
//				tiley = friendlyUnits.get(0).getPosition().getTiley();
//	
//				// White Highlight Valid Tiles for adding attack value
//					drawHighlight(out, Board.getBoard()[tiley][tilex], 1);
//			break;
//		// Player 2 - Entropic Decay - Reduce a non-avatar unit to 0 health
//		case 27 :
//		case 37 :
//			// All enemy units, except enemy's avatar (index = 0)
//			for (int i = 1; i < enemyUnits.size(); i++) {
//				tilex = enemyUnits.get(i).getPosition().getTilex();
//				tiley = enemyUnits.get(i).getPosition().getTiley();
//	
//				// Red Highlight Valid Tiles for Attack
//					drawHighlight(out, Board.getBoard()[tiley][tilex], 2);
//			}
//			break;
//		}
//	}
	
}
