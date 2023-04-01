package controllers;

import java.util.List;
import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Avatar;
import structures.basic.Card;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.ability.SpellThiefAbility;
import structures.basic.aiLogic.Attack;
import structures.basic.aiLogic.AvailableSteps;
import structures.basic.aiLogic.Move;

/**
 * This class provide actions for ai player to use.
 * 
 * @author Ying Ting Liu
 * @author Fong Wai Lam
 */

public class AIActions {	

	private final ActorRef out;
	private final GameState gameState;
	private final ActionController actionController;

	AvailableSteps available = new AvailableSteps();

	public AIActions(ActorRef out, GameState gameState) {
		this.out = out;
		this.gameState = gameState;
		this.actionController = gameState.currentGame.getActionController();
		this.available = new AvailableSteps();
	}

	
	public void attack() {
		
		available.calculate(gameState);		
		
		while (available.getUnitToAttacks() != null && !available.getUnitToAttacks().isEmpty()
				&& Avatar.avatar1.getHealth() > 0) {			

			List<Attack> attacks = available.getUnitToAttacks().get(0);
			
			Unit attacker = attacks.get(0).getAttacker();
			Attack selectedAttack = available.selectAttackPerUnit(attacks);
			
			Unit defender = selectedAttack.getDefender();

			if (attacker.getAttackChance() > 0) {
				BasicCommands.addPlayer1Notification(out, "AI attack!", 2);				

				// Attack and Move
				actionController.selectEnemyToAttack(attacker, defender);
				
				sleep(500);
				available.getUnitToAttacks().remove(0);
				available.calculate(gameState);
			}
		}
	}

	public void move() {
		available.clear();
		available.calculateMove(gameState);

		while (available.getMoves() != null && !available.getMoves().isEmpty() 
				&& Avatar.avatar1.getHealth() > 0) {
			Move move = available.getMoves().get(0);
			Unit unit = move.getUnit();

			List<Tile> positionTiles = move.getPositionTiles();
			int randomIndex = (int) (Math.random() * positionTiles.size());

			Tile randomTile = positionTiles.get(randomIndex);			

			// Move
			actionController.selectTileToMove(unit, randomTile.getTilex(), randomTile.getTiley());

			sleep(500);
			available.clear();
			available.calculateMove(gameState);
		}
	}

	
	public void summon() {
		List<Card> cardsList = available.aiChooseCard(gameState);		
		Card card = available.aiUnitCard(gameState, cardsList);
		
		if (card == null && card instanceof Spell) {
	        return;
	    }
		
		while(card!=null) {			

		    Tile tile = available.aiSummon(card, gameState);
		    if (tile == null) {
		        return;
		    }

		    boolean summon = gameState.currentGame.getGameController().summonUnit(card, tile);		    
		    if (summon) {	
		    	BasicCommands.addPlayer1Notification(out, "AI summon!", 2);
		    	sleep(300);
		        // Reduce mana
		        gameState.currentGame.getPlayerController().drainMana(card.getManacost());
		        sleep(300);
		        // Hand: Delete Card
		        gameState.currentPlayer.getHand().remove(card);
		        sleep(300);
		    }		   
		    // recalculate
		    cardsList.clear();
		    card = null;
		    
		    cardsList = available.aiChooseCard(gameState);		    
		    if(cardsList.size()>0) {		    	
		    	card = available.aiUnitCard(gameState, cardsList);
		    }
		    sleep(500);
		}

		
	}
	
	public void castSpell() {
		
		List<Card> cardsList = available.aiChooseCard(gameState);		
		if(cardsList == null) {
			return;
		}
		
		Card card = available.aiSpellCard(gameState, cardsList);
		if (card == null && !(card instanceof Spell)) {
	        return;
	    }

		Tile tile = available.aiSpellTile(out, gameState, card);

		boolean spellSuccess = gameState.currentGame.getGameController().castSpell(card, tile);

		if (spellSuccess) {
			// check spell thief
			SpellThiefAbility spellThiefAbility = new SpellThiefAbility();
			spellThiefAbility.perform(out, gameState);
			sleep(300);
			
			String string = card.getCardname() ;			
			BasicCommands.addPlayer1Notification(out, "AI spell: "+ string, 2);
	    	sleep(300);
	    	
			// Reduce mana
	        gameState.currentGame.getPlayerController().drainMana(card.getManacost());
	        sleep(300);
	        // Hand: Delete Card
	        gameState.currentPlayer.getHand().remove(card);
	        sleep(300);
		}
	}	
	
	// aiAvatar escape
	public void escape() {
		available.clear();
		
		int escapeChance = available.avatarEscapeChance(gameState);
		
		// When avatar2 can escape
		if (escapeChance != 0 && Avatar.avatar1.getHealth() > 0) {

			// Left the only routes to escape or health left half
			if (escapeChance <= 2 || Avatar.avatar2.getHealth() <= 10) {
				
				// Move
				Tile escapeTile = available.findEscapeRoute(gameState);

				actionController.selectTileToMove(Avatar.avatar2, escapeTile.getTilex(), escapeTile.getTiley());
			}
		}
	}

	private static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
