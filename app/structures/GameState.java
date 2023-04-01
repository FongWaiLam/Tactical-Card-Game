package structures;

import java.util.ArrayList;

import structures.basic.Avatar;
import structures.basic.Card;
import structures.basic.Game;
import structures.basic.Player;
import structures.basic.Unit;

/**
 * This class can be used to hold information about the on-going game. Its
 * created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 * 
 *         add addtional content
 * @author Fong Wei Lam
 * @author XueHui Chen
 * @author JingYi Xiang
 * @author YenNing Jia
 * @author Ying Ting Liu
 *
 */
public class GameState {

	public int turnNo = 1;// Start with turn 1
	public boolean endGame;

	public boolean gameInitalised = false;
	public boolean something = false;

	public boolean unitMoving = false;

	// Start with Human Player (Player 1)
	public Player currentPlayer = null;
	public Player waitingPlayer = null;
	public Game currentGame = null;
	public Card onClickedCard = null;
	public Unit selectedUnit;

	public int selectedHandPosition = -1;

}
