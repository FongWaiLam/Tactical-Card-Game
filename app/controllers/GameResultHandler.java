package controllers;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;

/**
 * This class handle the game result and release the game result for the user.
 * 
 * @author YenNing Jia
 * 
 */

public class GameResultHandler {

	public void checkResult(GameState gameState, ActorRef out) {
		// Opponent has 0 health, current player win.
		if (gameState.waitingPlayer.getHealth() == 0) {

			if (gameState.currentPlayer.getPlayerNo() == 1) {
				BasicCommands.addPlayer1Notification(out, "You Win!", 2000);
			} else {
				BasicCommands.addPlayer1Notification(out, "You Lose!", 2000);
			}
			gameState.endGame = true;
			
			// Current player has 0 health, lose.
		} else if (gameState.currentPlayer.getHealth() == 0) {

			if (gameState.currentPlayer.getPlayerNo() == 2) {
				BasicCommands.addPlayer1Notification(out, "You Win!", 2000);
			} else {
				BasicCommands.addPlayer1Notification(out, "You Lose!", 2000);
			}
			gameState.endGame = true;
		} 
	}

	public void checkResultWhenDrawCard(GameState gameState, ActorRef out) {

		// Current player has 0 card in deck when it draw from deck, lose.
		if (gameState.currentPlayer.getDeck().size() == 0) {

			if (gameState.currentPlayer.getPlayerNo() == 2) {
				BasicCommands.addPlayer1Notification(out, "You Win!", 2000);
			} else {
				BasicCommands.addPlayer1Notification(out, "You Lose!", 2000);
			}
			
			gameState.endGame = true;
		}
	}
	
}
