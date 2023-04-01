package structures.basic.ability;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Unit;

/**
 * Story # 28 Azurite Lion, Serpenti,  reset to 2 attack
 */

public class AttackTwiceAbility extends Ability{
	
	Unit unit;

	public AttackTwiceAbility(Unit unit) {
		super();
		this.unit = unit;
	}
	
	@Override
	public void perform(ActorRef out, GameState gameState) {
		unit.setAttackChance(2);
		unit.setMoveChance(2);
	}

}
