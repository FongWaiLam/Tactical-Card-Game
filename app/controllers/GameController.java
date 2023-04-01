package controllers;

import java.util.Set;
import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.EffectAnimation;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.ability.Ability;
import structures.basic.ability.AbilityType;
import structures.basic.ability.HealAvatarAbility;
import structures.basic.ability.SummonDrawCardAbility;
import utils.BasicObjectBuilders;
import utils.ScopeCompute;
import utils.StaticConfFiles;

/**
 * GameController class handles all the game logic for summon and casting spell
 * 
 * @author Ying Ting Liu
 * @author Xuehui-Chen
 * @author Fong Wai Lam
 * @author JingYi Xiang
 */

public class GameController {
	

	private final ActorRef out;
	private final GameState gameState;
	

	public GameController(ActorRef out, GameState gameState) {
		this.out = out;
		this.gameState = gameState;
	}
	
	/**
	 * Casts a spell on the given tile based on it's scope.
	 *
	 * @param card The card representing the spell being cast.
	 * @param tile The tile on which to cast the spell.
	 */
	public boolean castSpell(Card card, Tile tile) {

		Set<Tile> validSpellScope = ScopeCompute.spell(out, gameState, card);
		
		// Check if the tileClicked are within scope
		if (validSpellScope.contains(tile)) {
			
			Spell spellCard = (Spell) card;

			int spellHealth = spellCard.getSpellHealth();
			int spellAttack = spellCard.getSpellAttack();
			Boolean spellKillUnit = spellCard.isSpellKillUnit();
			
			// Get the clickedUnit on Tile
			Unit unitOnTile = gameState.currentPlayer.unitOnTile(tile) != null ? gameState.currentPlayer.unitOnTile(tile) : gameState.waitingPlayer.unitOnTile(tile);
			
			// Check if the spell is health-related
			if (spellHealth != 0) {

				// Spell that deal health
				if (spellHealth < 0) {
					
					// Play effect animation (Reduce health)

					// Player 1 - Truestrike - Deal 2 damage to an enemy unit
					// Spell : Truestrike - Effect : Inmolation
					// Applicable to: enemyTiles
					effectAnimation(4, tile);
					
					unitOnTile.beAttacked(out, gameState, (-spellHealth));

					// Check if unit has died or not
					// Died: deleteUnit()
					unitOnTile.unitDeathCheck(out, gameState);

				} else if (spellHealth > 0) {
					
					// Play effect animation (Heal)
					// Spell : Sundrop Elixir : Buff
					// Player 1 - Sundrop Elixir - Add +5 health to a Unit. Not take a unit over its
					// starting health
					// Applicable to: friendlyTiles
					effectAnimation(2, tile);
					
					unitOnTile.beHealed(out, spellHealth);
				}
				return true;
				
			} else if (spellAttack != 0) {
				
				unitOnTile.strengthenAttack(out, spellAttack);

				// Play effect animation (Gain Attack)
				// Spell : Staff of Y kir - Effect : Buff
				// Player 2 - Staff of Y’Kir - Add +2 attack to your avatar
				// Applicable to: Current player's avatar
				effectAnimation(2, tile);
				
				return true;
			} else if (spellKillUnit == true) {				

				// Play effect animation (Reduce health)

				// Player 2 - Entropic Decay - Reduce a non-avatar unit to 0 health
				// Applicable to: All enemy units, except enemy's avatar(index = 0)
				effectAnimation(4, tile);	
				
				unitOnTile.beAttacked(out, gameState, (-spellHealth));
				unitOnTile.setHealth(0);
				
				// Check if unit has died or not
				// Died: deleteUnit()
				unitOnTile.unitDeathCheck(out, gameState);
				return true;
			}
		}
		// Spell Fail
		return false;
	}

	public boolean summonUnit(Card card, Tile tile) {

		Set<Tile> validSummonScope = ScopeCompute.summon(gameState, card);
		boolean validSummon = validSummonScope.contains(tile);
		
		if (validSummon) {
			
			checkOnSummonAbility(card);
			
			Unit unit = BasicObjectBuilders.loadUnit(card.cardStaticConfFile(), card.getId(), Unit.class);
			unit.setPositionByTile(tile);
			// set name
			String name = card.getCardname();
			unit.setName(name);
			unit.setAbilities();
			
			// draw unit
			BasicCommands.drawUnit(out, unit, tile);
			sleep(100);
			
			// set attack
			int attack = card.getBigCard().getAttack();
			unit.setAttack(attack);
			// set health
			int health = card.getBigCard().getHealth();
			
			refreshHealthAndAttack(unit, health, attack);
			unit.setHealth(health);
			unit.setMaxHealth(health);
			
			// Story 11.1 Unit Action: Just Summon Not Move
			unit.setAttackChance(0);
			unit.setMoveChance(0);

			// Play effect animation - (1) Summon a unit
			effectAnimation(1, tile);

			// Add an unitOnBoard
			gameState.currentPlayer.getUnitsOnBoard().add(unit);
	
			
		}else{
			BasicCommands.addPlayer1Notification(out, "invalid summon", 2);
		}
		
		return validSummon;

	}
	
	public void checkOnSummonAbility(Card card) {

		// Story # 26- Heal (Azure Herald) (humanPlayer)
		if(Ability.hasSummonAbility(card, AbilityType.onSummonHeal)) {
		      BasicCommands.addPlayer1Notification(out, "onSummonHeal on Avatar", 2);
		      sleep(2000);
		      HealAvatarAbility healAvatarAbility = new HealAvatarAbility();
		      healAvatarAbility.perform(out, gameState);
		}
		
		if(Ability.hasSummonAbility(card, AbilityType.onSummonDrawCard)) {
			BasicCommands.addPlayer1Notification(out, "onSummonDrawCard", 2);
			sleep(2000);
			SummonDrawCardAbility summonDrawCardAbility = new SummonDrawCardAbility();
			summonDrawCardAbility.perform(out, gameState);
		}
	}

	// Play effect animations
	// (1) Summon a unit
	// (2) Spell : Sundrop Elixir or Staff of Y kir - Effect : Buff
	// (3) Spell : Entropic Decay - Effect : Martyrdom
	// (4) Spell : Truestrike - Effect : Inmolation
	public void effectAnimation(int effectNo, Tile tile) {

		String[] effects = { StaticConfFiles.f1_summon, StaticConfFiles.f1_buff, StaticConfFiles.f1_martyrdom,
				StaticConfFiles.f1_inmolation };

		EffectAnimation ef = BasicObjectBuilders.loadEffect(effects[effectNo - 1]);
		BasicCommands.playEffectAnimation(out, ef, tile);
	}


	public void refreshHealthAndAttack(Unit unit, int health, int attack) {
		// set attack
		BasicCommands.addPlayer1Notification(out, "setUnitAttack", 2);
		BasicCommands.setUnitAttack(out, unit, attack);
		sleep(100);
		// set health
		BasicCommands.addPlayer1Notification(out, "setUnitHealth", 2);
		BasicCommands.setUnitHealth(out, unit, health);
		sleep(100);
	}

	public void refreshHealth(Unit unit, int health) {
		BasicCommands.addPlayer1Notification(out, "setUnitHealth", 2);
		BasicCommands.setUnitHealth(out, unit, health);
		System.out.println("refresh health");
		sleep(100);
	}
	
	// Play a card
	public boolean playCard(Tile tile) {
		Card onClickedCard = gameState.onClickedCard;
		int handPosition = gameState.selectedHandPosition;

		if (gameState.currentPlayer.unitOnTile(tile) == null && !(onClickedCard instanceof Spell)) {
			// Check if summon successfully
			if (!summonUnit(onClickedCard, tile)) {
				// unsuccessful
				return false;
			}
		} else if ((gameState.currentPlayer.unitOnTile(tile) != null || gameState.waitingPlayer.unitOnTile(tile) != null) 
				&& onClickedCard instanceof Spell) {
			
				// Check if cast spell successfully
				if (!castSpell(onClickedCard, tile)) {
					return false;
				}
	
		} else {
			// Failed to play card
			return false;
		}
		// Successfully played a card
		// Reduce mana
		gameState.currentGame.getPlayerController().drainMana(onClickedCard.getManacost());
		
		// Hand: Delete Card
		gameState.currentPlayer.getHand().remove(handPosition);
		// Display: Delete Card
		BasicCommands.deleteCard(out, handPosition);

		// True if play a card successfully (Return false previously if cannot meet requirements)
		return true;

	}
	
	private static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}