package structures.basic.ability;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.EffectAnimation;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class RangeAttackAbility extends Ability {
	
	Unit unit;
	Unit targetUnit;
	
	public RangeAttackAbility(Unit unit, Unit targetUnit) {
		super();
		this.unit = unit;
		this.targetUnit = targetUnit;
	}
	
	@Override
	public void perform(ActorRef out, GameState gameState) {
		// playProjectileAnimation
		EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
		BasicCommands.playProjectileAnimation(out, projectile, 0, unit.getTile(), targetUnit.getTile());
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}	
	}

}
