package structures.basic;

import java.util.List;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.BasicObjectBuilders;

/**
 * The Board class represents the game board and contains methods for building 
 * and resetting the board, as well as 
 * checking if a friendly or enemy unit is on a given tile, and getting a path between two tiles.
 * 
 * @author Fong Wai Lam
 * @author Ying Ting Liu
 */

public class Board {

	private static final int ROWS = 9;
    private static final int COLS = 5;
    
	private static Tile[][] tiles =  new Tile[COLS][ROWS];

	
	public static Tile[][] getBoard() {
		return tiles;
	}
	
	public static void buildBoard(ActorRef out) {
        for (int y = 0; y < COLS; y++) {
            for (int x = 0; x < ROWS; x++) {
                tiles[y][x] = BasicObjectBuilders.loadTile(x, y);
                BasicCommands.drawTile(out, tiles[y][x], 0);
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	/**
     * Story#8 - After the Move, Move and Attack, Summon, the board can be unhighlighted
     * Resets the board by redrawing each tile on the screen.
     *
     * @param out 
     */	
	public static void resetWholeBoard(ActorRef out) {
        for (int y = 0; y < COLS; y++) {
            for (int x = 0; x < ROWS; x++) {
                BasicCommands.drawTile(out, tiles[y][x], 0);
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
}
