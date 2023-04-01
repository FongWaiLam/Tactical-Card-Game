package structures.basic.ability;


import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Player;
import structures.basic.Unit;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/**
 * This is the base class of ability
 * @author Fong Wei Lam
 */

public class Ability {	
	
	// Check if a unit has this ability
	public static boolean hasAbility(Unit unit, AbilityType checkAbility) {
		
		Set<AbilityType> abilities = unit.getAbilities();
		
		if (abilities == null ) {			
			return false;
		} else {
			for (AbilityType ability :abilities) {
				
				if (ability == checkAbility) {					
					return true;
				}
			}
		}
		return false;
	}

	// Check if a card has this ability when summon
	public static boolean hasSummonAbility(Card card, AbilityType checkAbility) {
		Set<AbilityType> abilities = card.getSummonAbilities();
		if (abilities == null ) {
			return false;
		} else {
			for (AbilityType ability :abilities) {
				if (ability == checkAbility) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void perform(ActorRef out, GameState gameState) {}

	public void perform(ActorRef out, GameState gameState, Player ownerPlayer) {}


}
