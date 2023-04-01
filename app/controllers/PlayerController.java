package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Avatar;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Hand;
import structures.basic.HumanPlayer;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.ability.SpellThiefAbility;
import structures.basic.aiLogic.Attack;
import structures.basic.aiLogic.AvailableSteps;

/**
 * 
 * The PlayerController class is responsible for managing the player related
 * info in the game, including end turn, switch players, increase and decrease
 * mana. 
 * 
 * @author Ying Ting Liu
 * @author Fong Wai Lam
 * @author YenNing Jia
 * 
 */
public class PlayerController {

	Logger logger = LoggerFactory.getLogger(PlayerController.class);

	private final ActorRef out;
	private final GameState gameState;

	public PlayerController(ActorRef out, GameState gameState) {
		this.out = out;
		this.gameState = gameState;
		
	}	

	public void endTurn() {
		
		Board.resetWholeBoard(out);

		clearMana(0);

		// Story #1 - draw an extra card at the end of each turn.
		// Draw card success
		if (gameState.currentGame.getCardController().drawCard()) {
			Hand hand = gameState.currentPlayer.getHand();
			System.out.println("draw card hand:" + hand.toString());
		} else {
		// Draw card fail, end game
			GameResultHandler gameResultHandler = new GameResultHandler();
			gameResultHandler.checkResultWhenDrawCard(gameState, out);
		}
		
		if (gameState.currentPlayer == AIPlayer.getInstance()) {
			// add turn
			gameState.turnNo++;
		}
		
		// ----------------------Previous Player-------------------
		switchPlayerTurn();
		// -----------------------Next Player----------------------
		
		// AI has not drawn card previously
		if (gameState.turnNo == 1) { // the first AI turn draw 3 cards.
			for (int i = 0; i < 3; i++) {
				AIPlayer.getInstance().drawCard();
			}
		}
		
		gameState.currentPlayer.resetUnitState(out, gameState);


		gainMana(gameState.turnNo);		

		if (gameState.currentPlayer == AIPlayer.getInstance()) {
			BasicCommands.addPlayer1Notification(out, "AI is thinking...", 2);
			sleep(1500);

			// perform AI actions
			performAIActions();
			sleep(700);
			BasicCommands.addPlayer1Notification(out, "AI is done!", 2);
			sleep(1200);
			BasicCommands.addPlayer1Notification(out, "Your turn: " + gameState.turnNo, 2);
			sleep(1000);

		}

	}

	public void switchPlayerTurn() {
		Player tempWaitingPlayer = gameState.currentPlayer;
		gameState.currentPlayer = gameState.waitingPlayer;
		gameState.waitingPlayer = tempWaitingPlayer;
	}
	
	public void performAIActions() {
		
		AIActions aiActions = new AIActions(out, gameState);

		if(gameState.turnNo<3) {
			aiActions.escape();
			aiActions.attack();	
			aiActions.move();
			aiActions.summon();
			aiActions.castSpell();
		} else {
			aiActions.castSpell();
			aiActions.escape();
			aiActions.attack();	
			aiActions.move();			
			aiActions.summon();
		}
		
		endTurn();
		
	}	

	public void gainMana(int turnNo) {
		Player player = gameState.currentPlayer;

		player.setMana(turnNo + 1);
		displayMana(player);

	}

	public void drainMana(int manaNo) {
		Player player = gameState.currentPlayer;

		player.setMana(player.getMana() - manaNo);
		displayMana(player);

	}

	public void clearMana(int manaNo) {

		Player player = gameState.currentPlayer;

		player.setMana(0);
		displayMana(player);

	}

	public void displayMana(Player player) {

		if (player.getPlayerNo() == 1) {
			BasicCommands.setPlayer1Mana(out, player);
			sleep(100);
		} else if (player.getPlayerNo() == 2) {
			BasicCommands.setPlayer2Mana(out, player);
			sleep(100);
		}
	}

	private static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
