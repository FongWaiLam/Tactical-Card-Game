package structures.basic;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.ability.Ability;
import structures.basic.ability.AbilityType;
import structures.basic.ability.AttackTwiceAbility;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 * @author Xuehui-Chen
 * @author Fong Wai Lam
 * @author YenNing Jia 
 * @author Ying Ting Liu
 *
 */
public class Player {

	// Original fields
	private int health;
	private int mana;
	
	// self defined fields
	@JsonIgnore
	private int playerNo;
	// Units summoned on board; Including Avatar; Max 16 + 1 units can be summoned
	@JsonIgnore
	private ArrayList<Unit> unitsOnBoard = new ArrayList<>(17);
	@JsonIgnore
	public boolean drawCard;
	@JsonIgnore
	public int deckRemainingCard = 20;
	@JsonIgnore
	public int handRemainingCard;		
	@JsonIgnore
	private List<Card> deck;
	@JsonIgnore
	private Hand hand;	
	
	// Original Codes
	public Player() {
		super();
		this.health = 20;
		this.mana = 0;
	}
	
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getMana() {
		return mana;
	}
	
	public void setMana(int mana) {
		this.mana = mana;
	}
	
	public int getPlayerNo() {
		return playerNo;
	}
	
	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}
	
	public ArrayList<Unit> getUnitsOnBoard() {
		return unitsOnBoard;
	}	
	
	public List<Card> getDeck() {
		return deck;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}
	
	public void setDeck(List<Card> playerCards) {
		deck = playerCards;
		
	}
	
	public boolean drawCard() {
		
		return this.hand.drawCardFromDeck(deck);
	}

	public boolean isDrawCard() {
		return drawCard;
	}

	public void setDrawCard(boolean drawCard) {
		this.drawCard = drawCard;
	}	
	
	public boolean deckHasRemainingCard() {
		if (deck.size() != 0) {
			return true;
		}
		return false;
	}
	
	public boolean handHasRemainingCard() {
		return hand.isEmpty();
	}
	
	// Refresh Health when necessary
	public void refreshHealth(ActorRef out) {
		if (playerNo == 1) {
			BasicCommands.setPlayer1Health(out, this);
			try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		} else if (playerNo == 2) {
			BasicCommands.setPlayer2Health(out, this);
			try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	// After each turn, unit status reset
	public void resetUnitState(ActorRef out, GameState gameState) {
		for (Unit unit : unitsOnBoard) {
			unit.setMoveChance(1);
			
			// Story # 28 Azurite Lion, Serpenti:  reset to 2 attack
			if (Ability.hasAbility(unit, AbilityType.attackTwice)) {
				
				AttackTwiceAbility attackTwiceAbility = new AttackTwiceAbility(unit);
				attackTwiceAbility.perform(out, gameState);
				System.out.println(unit.getName()+"attack twice" + unit.getAttackChance()+"health "+unit.getMoveChance());
			} else {
				
				unit.setAttackChance(1);
			}
		}
	}
	
	// Get Unit on the tile
	public Unit unitOnTile(Tile tile) {
		for (Unit unit: unitsOnBoard) {
			if (unit.OnTile(tile)) {
				return unit;
			}
		}		
		return null;
	}
	
	// Get Unit on the tile
	public Unit unitOnTile(int tilex, int tiley) {
		for (Unit unit: unitsOnBoard) {
			if (unit.OnTile(tilex, tiley)) {
				return unit;
			}
		}		
		return null;
	}

}
