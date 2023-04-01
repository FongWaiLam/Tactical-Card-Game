package structures.basic;

import utils.OrderedCardLoader;

import java.util.HashMap;

import structures.GameState;
/**
 * This is the AI player extends player 
 * using Singleton Design Pattern to ensure only one instance in the gamestate.class.
 * 
 * @author Fong Wai Lam
 */

public class AIPlayer extends Player{
	// Singleton Design Pattern - Ensure only one instance is created.
		private static AIPlayer aiPlayer = new AIPlayer();

		// Singleton Design Pattern - Private constructor
		private AIPlayer() {
			super.setPlayerNo(2);
			super.setHealth(20);
			
			super.getUnitsOnBoard().add(Avatar.avatar2);
			
			super.setDeck(OrderedCardLoader.getPlayer2Cards());
			// Special Config for Spell cards
			OrderedCardLoader.spellCardMapping(getDeck());
			super.setHand(new Hand());
		}

		// Singleton Design Pattern - get the only one instance
		public static AIPlayer getInstance() {
			return aiPlayer;
		}
		
		public static void RefreshAIPlayer() {
			aiPlayer = new AIPlayer();
		}

}
