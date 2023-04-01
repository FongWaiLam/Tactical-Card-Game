package structures.basic.ability;

import java.util.ArrayList;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Unit;

public class AvatarDamagedAbility extends Ability {
	
	@Override
	public void perform(ActorRef out, GameState gameState, Player ownerPlayer) {
		
		ArrayList<Unit> units = ownerPlayer.getUnitsOnBoard();

		System.out.println("perform test");
		for (Unit unit : units) {
			System.out.println("travse units");
			if (Ability.hasAbility(unit, AbilityType.avatarDamaged)) {	

				System.out.println("check AvatarDamagedAbility!");
				int amount = 2;
				unit.strengthenAttack(out, amount);
				break;
			}
		}
	}

}
