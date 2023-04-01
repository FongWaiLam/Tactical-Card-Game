package structures.basic.ability;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Player;
import structures.basic.Unit;

/**
 * Story # 26- Heal (Azure Herald) (humanPlayer)
 * When Azure Herald is summoned(cost 2 mana), give your avatar +3 health (maximum 20)
 * 
 * @author JingYi
 */

public class HealAvatarAbility extends Ability {
	
	@Override
	public void perform(ActorRef out, GameState gameState) {
		//When Azure Herald is summoned(cost 2 mana), give your avatar +3 health (maximum 20)
		 Unit target=Avatar.avatar1;
		 Player player=gameState.currentPlayer;
		 target.setMaxHealth(20);
		 if (target.getHealth() < target.getMaxHealth()) {
	         target.beHealed(out, 3);
			// Refresh avatar's health to player
			player.setHealth(target.getHealth());
			player.refreshHealth(out);
	   }
	}

}
