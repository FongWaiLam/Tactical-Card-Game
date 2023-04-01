package structures.basic;

import utils.OrderedCardLoader;

/**
 * This is the Human player extends player 
 * using Singleton Design Pattern to ensure only one instance in the gamestate.class.
 * 
 * @author Fong Wai Lam
 */

public class HumanPlayer extends Player {

	// Singleton Design Pattern - Ensure only one instance is created.
	private static HumanPlayer humanPlayer;

	// Singleton Design Pattern - Private constructor
	private HumanPlayer() {
		
		super.setPlayerNo(1);
		super.setHealth(20);
		
		super.getUnitsOnBoard().add(Avatar.avatar1);
		
		super.setDeck(OrderedCardLoader.getPlayer1Cards());
		// Special Config for Spell cards
		OrderedCardLoader.spellCardMapping(getDeck());
		super.setHand(new Hand());
	};

	// Singleton Design Pattern - get the only one instance
	public static HumanPlayer getInstance() {
		if (humanPlayer == null) {
			return new HumanPlayer();
		}
		return humanPlayer;
	}
	
	public static void RefreshHumanPlayer() {
		humanPlayer = new HumanPlayer();

	}
	
}
