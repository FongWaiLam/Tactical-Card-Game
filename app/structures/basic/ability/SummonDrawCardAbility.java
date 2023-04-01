package structures.basic.ability;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.HumanPlayer;

// Blaze Hound
public class SummonDrawCardAbility extends Ability {
	
	@Override
	public void perform(ActorRef out, GameState gameState) {
		AIPlayer.getInstance().drawCard();
		HumanPlayer.getInstance().drawCard();	
		gameState.currentGame.getCardController().showCardOnHand();
	}

}
