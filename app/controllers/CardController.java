package controllers;

import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Card;
import structures.basic.HumanPlayer;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.ability.Ability;
import structures.basic.ability.AbilityType;
import utils.HighlightDisplay;
import utils.ScopeCompute;

/**
 * 
 * The CardController class is responsible for managing the cards in the game,
 * including showing cards on hand, drawing cards, handling clicked cards, and
 * removing cards from the hand.
 * 
 * @author Ying Ting Liu
 * @author Xuehui-Chen
 * @author Fong Wai Lam
 */
public class CardController {

	private final ActorRef out;
	private final GameState gameState;

	public CardController(ActorRef out, GameState gameState) {
		this.out = out;
		this.gameState = gameState;
	}

	public void showCardOnHand() {
		int position = 1;
		for (Card card : HumanPlayer.getInstance().getHand().getHandOfCards()) {

			BasicCommands.drawCard(out, card, position++, 0);
			sleep(100);
		}
	}

	public boolean drawCard() {
		if (gameState.currentPlayer == HumanPlayer.getInstance()) {
			if (HumanPlayer.getInstance().getHand().isFull() == true) {
				sleep(100); // show the human round message first and then show hands full
				BasicCommands.addPlayer1Notification(out, "Hands Full, drop card", 2);
				sleep(100);
			}

			// human player draw card
			if (HumanPlayer.getInstance().drawCard()) {
				sleep(100);
				// Show cards. only human player need to show cards on Hand
				showCardOnHand();
				return true;
			} else {
				return false;
			}

		} else if (gameState.currentPlayer == AIPlayer.getInstance()) {
			// ai player draw card
			if (AIPlayer.getInstance().drawCard()) {
				sleep(100);
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public void onClickedCard(Card onClickedCard, int handPosition) {

		// Check if the player has enough mana
		if (gameState.currentPlayer.getMana() >= onClickedCard.getManacost()) {

			// Record the card clicked
			gameState.onClickedCard = onClickedCard;
			gameState.selectedHandPosition = handPosition;

			// Determine whether it is an unit or spell card
			if (!(gameState.onClickedCard instanceof Spell)) {

				// An unit Card
				// validSummonScope for evaluating user's next TileClicked Event
				Set<Tile> validSummonScope = ScopeCompute.summon(gameState, onClickedCard);

				// Display summon highlight
				HighlightDisplay.drawSetHighlight(out, validSummonScope, 1);

			} else {

				// A spell Card
				// Get valid scope for this spell card
				// validSpellScope for evaluating user's next TileClicked Event and invoke spell
				// effect
				Set<Tile> validSpellScope = ScopeCompute.spell(out, gameState, onClickedCard);

				// Story# 24
				// Highlight valid scope of the spell
				HighlightDisplay.drawSpellHighlight(out, validSpellScope, onClickedCard);
			}

		} else {
			BasicCommands.addPlayer1Notification(out, "Not enough mana for this card: " + onClickedCard.getCardname(),
					2);
			sleep(100);
			// Display: Refresh selected card
			gameState.currentGame.getCardController().showCardOnHand();
		}

	}

	public void removeCardFromHand(int cardClickedId) {
		HumanPlayer.getInstance().getHand().remove(cardClickedId);
	}

	private static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}