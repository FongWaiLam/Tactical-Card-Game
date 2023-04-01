package structures.basic.ability;

import java.util.ArrayList;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Unit;

/**
 * When a spell is played, the opposing units should be checked to see if
 * SpellThief should be triggered for any of their units. 
 * eg. Pureblade Enforcer gains +1/+1 
 * 
 * @author Ying Ting Liu
 */	

public class SpellThiefAbility extends Ability {	
	
	
	@Override
	public void perform(ActorRef out, GameState gameState) {
		final int  spThiefHealth = 1;
		final int  spThiefAttack = 1;
		
		ArrayList<Unit> units = gameState.waitingPlayer.getUnitsOnBoard();
		for (Unit unit : units) {
			if (Ability.hasAbility(unit, AbilityType.spellThief)) {
				// set attack and health

				int addAttack = unit.getAttack() + spThiefAttack;
				int addHealth = unit.getHealth()+spThiefHealth;
				
				unit.setAttack(addAttack);
				unit.setHealth(addHealth);
				
				// show attack and health in front end
				gameState.currentGame.getGameController().refreshHealthAndAttack(unit, addHealth, addAttack);
				BasicCommands.addPlayer1Notification(out, "spellThief!", 2);
				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
				return;
			}			
		}	
	}

}
