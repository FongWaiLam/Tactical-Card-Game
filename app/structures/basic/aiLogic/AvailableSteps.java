package structures.basic.aiLogic;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.AIPlayer;
import structures.basic.Card;
import structures.basic.Avatar;
import structures.basic.HumanPlayer;
import structures.basic.Spell;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.ScopeCompute;
/**
 * This class provide a calculation of ai available steps.
 * This part of code is inspire by github project: aps2019project/Duelers
 * link below: https://github.com/aps2019project/Duelers/tree/master/Client/src/models/game/availableActions
 * 
 * We rearrange it to fit our own design and add functionalities
 * 
 * Pair: Using Pairs in Java
 * https://www.baeldung.com/java-pairs
 * 
 * @author Ying Ting Liu
 * @author Fong Wei Lam
 */
public class AvailableSteps {

	private List<Move> moves = new ArrayList<Move>();
//	private List<Attack> attacks = new ArrayList<Attack>();	
	
	private List<List<Attack>> unitToAttacks = new ArrayList<>();
	private List<Attack> attacksPerUnit = new ArrayList<Attack>();	
	
	
	private List<Escape> escapes = new ArrayList<Escape>();
	
	AIPlayer aiPlayer = AIPlayer.getInstance();
	HumanPlayer humanPlayer = HumanPlayer.getInstance();
	List<Unit> humanUnits = humanPlayer.getUnitsOnBoard();
	List<Unit> aiUnits = aiPlayer.getUnitsOnBoard();

	public AvailableSteps() {
		
	}

	public void calculate(GameState gameState) {
		clear();
		calculateAttack(gameState);
		calculateMove(gameState);
	}

	public void clear() {
//		this.attacks.clear();
		this.escapes.clear();
		this.unitToAttacks.clear();
		this.moves.clear();
	}
	
	
	public void calculateAttack(GameState gameState) {

		if (Avatar.avatar1.getHealth() <= 0) {
			return;
		}
		
		Map<Tile, Unit> humanMap = sortByHealth(humanUnits);
		
        for (Unit unit : aiUnits) {
        	
            if (unit.getAttackChance()<=0) continue;
            if (unit == Avatar.avatar2 && Avatar.avatar2.getHealth() <= 10) continue; 
            
            attacksPerUnit.clear();
            Set<Tile> targets = gameState.currentGame.getActionController().getValidAttackScope(unit);
            
            // prioritize avatar target
            for(Tile tile: targets) {
            	Unit hAvatar = Avatar.avatar1; // Human
            	Tile hAvatarTile = Avatar.avatar1.getTile();
            	if(tile.equals(hAvatarTile)) {
            		this.attacksPerUnit.clear();
            		attacksPerUnit.add(new Attack(unit, hAvatar,20));
            		break; // if target avatar no other option.
            	}
            	
            	for(Tile humanTile: humanMap.keySet()) {
            		Unit targetUnit = humanMap.get(humanTile);
            		if(humanTile.equals(tile)) { 
            			int weight = targetUnit.getAttack();
            			if(targetUnit.getAbilities()!=null) { // killing target with ability weight more
            				weight = weight + targetUnit.getAbilities().size();
            			}
            			attacksPerUnit.add(new Attack(unit, targetUnit, weight));            			
            		}
            		
            	}
            }
            if (attacksPerUnit.size() > 0) {
                unitToAttacks.add(attacksPerUnit);
            }
        }
    }
	
	// Decide attack target for each unit
	public Attack selectAttackPerUnit(List<Attack> attacks) {
		
		Collections.sort(attacks, new Comparator<Attack>() {
            @Override
            public int compare(Attack a1, Attack a2) {
                return Integer.compare(a1.getWeight(), a2.getWeight()); // Sort in desc order
            }
        });
		
		for (Attack attack : attacks) {
			if (attack.isDieAfterAttack() != false) {
				return attack;
			}
		}
		return attacks.get(0);
	}

	public void calculateMove(GameState gameState) {
		if (Avatar.avatar1.getHealth() <= 0) {
			return;
		}
		
		Unit avatar = gameState.waitingPlayer.getUnitsOnBoard().get(0);
		Tile avatarTile = avatar.getTile();
		int avaX = avatarTile.getTilex();
		int avaY = avatarTile.getTiley();

	    for (Unit unit : aiUnits) {
	    	
	        if (unit.getAttackChance() <= 0) continue;
	        
	        List<Tile> positionTiles = new ArrayList<Tile>();
	        Move move = new Move(unit, positionTiles);

	        Set<Tile> targets = gameState.currentGame.getActionController().getValidMoveScope(unit);
	        
	        
	        // No move available
	        if (targets == null) {
	        	return;
	        }
	        
	        int minSteps = Integer.MAX_VALUE;	        

			for (Tile tile : targets) {
				int targetX = tile.getTilex();
				int targetY = tile.getTiley();

				Pair<Integer, Direction> distance = distance(targetX, targetY, avaX, avaY);
				int steps = distance.getLeft();
				Direction direction = distance.getRight();

				if (steps < minSteps) {
					minSteps = steps;
					positionTiles.clear(); // clear previous positions
					positionTiles.add(tile);

					if (direction != null) {
						move.setDirection(direction);
					}
				}
			}
			// add to list
			moves.add(move);
		}
	}

	public List<Card> aiChooseCard(GameState gameState) {
		
		if (Avatar.avatar1.getHealth() <= 0) {
			return null;
		}
		
		Card[] handOfCards = gameState.currentPlayer.getHand().getHandOfCards();

		List<Card> choosedCards = new ArrayList<>();		
		for (Card card : handOfCards) {

			if (card!=null && card.getManacost() <= aiPlayer.getMana()) {
				choosedCards.add(card);
			}
		}
		
		return choosedCards;
	
	}	
	
	public Card aiUnitCard(GameState gameState, List<Card> choosedCards) {

		if(choosedCards==null|| choosedCards.size()==0) {			
			return null;
		}
		System.out.println("what's inside choose card? "+choosedCards.toString()+ "no1. "+choosedCards.size());
		
		Card choosedCard = null;

		for (Card card : choosedCards) {
			if(card instanceof Spell) {
				continue;
			}
			if (choosedCard == null ||card.getManacost() >= choosedCard.getManacost()) {
				choosedCard = card;
			}
		}		
		return choosedCard;
	}
	
	public Tile aiSummon(Card choosedCard, GameState gameState) {
		Set<Tile> summonScope = ScopeCompute.summon(gameState, choosedCard);
		Tile humanAvatarTile = humanPlayer.getUnitsOnBoard().get(0).getTile();
		
		int avaX = humanAvatarTile.getTilex();
		int avaY = humanAvatarTile.getTiley();
		
		int minSteps = Integer.MAX_VALUE;
		
		Map<Tile, Integer> tileToDistance = new HashMap<>();
		for(Tile tile: summonScope) {
			int cardTileX=tile.getTilex();
			int cardTileY=tile.getTiley();
			
			Pair<Integer, Direction> distance = distance(cardTileX, cardTileY, avaX, avaY);
			int steps = distance.getLeft();
			Direction direction = distance.getRight();

			if (steps < minSteps) {
				minSteps = steps;
				tileToDistance.clear(); // clear previous positions
				tileToDistance.put(tile,steps);
			}
			
		}
		
		Tile tileWithLargestValue = null;
	    int largestValue = Integer.MIN_VALUE;
	    for(Map.Entry<Tile, Integer> entry : tileToDistance.entrySet()) {
	        Tile tile = entry.getKey();
	        int value = entry.getValue();
	        if(value > largestValue) {
	            largestValue = value;
	            tileWithLargestValue = tile;
	        }
	    }

	    return tileWithLargestValue;
		
	}
	
	public Card aiSpellCard(GameState gameState, List<Card> choosedCards) {

	    if (choosedCards == null || choosedCards.size()==0) {
	        return null;
	    }

	    System.out.println("Spell: what's inside choose card? " + choosedCards.toString() + " no1. " + choosedCards.size());

	    Card choosedCard = null;
	    for (Card card : choosedCards) {
	    	
	    	if(card instanceof Spell) {	        	
		        
		        if (choosedCard == null || card.getManacost() >= choosedCard.getManacost()) {
		            choosedCard = card;
		        }
	        }
	        
	    }

	    return choosedCard;
	}
	
	public Tile aiSpellTile(ActorRef out, GameState gameState, Card card ) {

	    Set<Tile> summonScope = ScopeCompute.spell(out, gameState, card);
	    System.out.println("Tiles in the set:");
	    for (Tile tile : summonScope) {
	        System.out.println(tile.getTilex()+","+tile.getTiley());
	    }

	    // Shuffle the Set<Tile>
	    List<Tile> shuffledScope = new ArrayList<>(summonScope);
	    Collections.shuffle(shuffledScope);
	    Tile tile = shuffledScope.isEmpty() ? null : shuffledScope.get(0);
	    
	    return tile;
	}

	/**
	 * Calculates the Manhattan distance between two points and the direction 
	 * to move from the starting point to reach the ending point. 
	 * return a Pair object containing the Manhattan distance between the two points 
	 * and the direction to move from the starting point to reach the ending point
	 * 
	 * Resource: Using Pairs in Java
	 * https://www.baeldung.com/java-pairs
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @return
	 */
	public Pair<Integer, Direction> distance(int startX, int startY, int endX, int endY) {
		int dx = endX - startX;
		int dy = endY - startY;
		// If the movement is only in one dimension, 
		// return the distance in that direction and the corresponding direction
		if (dx == 0) {
			return dy > 0 ? Pair.of(dy, Direction.DOWN) : Pair.of(-dy, Direction.UP);
		} else if (dy == 0) {
			return dx > 0 ? Pair.of(dx, Direction.RIGHT) : Pair.of(-dx, Direction.LEFT);
		} else {
			// If the movement is in both dimensions, return the Manhattan distance and no direction			
			return Pair.of(Math.abs(dx) + Math.abs(dy), null);
		}
	}

	public static LinkedHashMap<Tile, Unit> sortByHealth(List<Unit> units) {
        Collections.sort(units, new Comparator<Unit>() {
            @Override
            public int compare(Unit u1, Unit u2) {
                return Integer.compare(u2.getHealth(), u1.getHealth()); // Sort in asc order
            }
        });
        LinkedHashMap<Tile, Unit> map = new LinkedHashMap<Tile, Unit>();
        for (Unit unit : units) {
            map.put(unit.getTile(), unit);
        }
        return map;
    }
	
	public int avatarEscapeChance(GameState gameState) {
		Set<Tile> escapeTiles = gameState.currentGame.getActionController().getValidMoveScope(Avatar.avatar2);
		if (escapeTiles == null || escapeTiles.isEmpty()) {
			return 0;
		}
		return escapeTiles.size();
	}
	
	// A tile has least units
	public Tile findEscapeRoute(GameState gameState) {
		
		Set<Tile> escapeTiles = gameState.currentGame.getActionController().getValidMoveScope(Avatar.avatar2);
		
		if (escapeTiles == null || escapeTiles.isEmpty()) {
			return null;
		}
		
		if (escapeTiles.size() == 1) {
			return escapeTiles.iterator().next();
		}
		Set<Tile> surroundingEnemy = null;
		
		for (Tile tile : escapeTiles) {
			surroundingEnemy = ScopeCompute.unitMovedCanAttack(gameState,tile.getTilex(), tile.getTiley());
			escapes.add(new Escape(tile, surroundingEnemy.size()));
		}
		
		Collections.sort(escapes);
		
		return escapes.get(0).getTile();
		
	}

	public List<Move> getMoves() {
		return moves;
	}

	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}

	public List<List<Attack>> getUnitToAttacks() {
		return unitToAttacks;
	}

	public void setUnitToAttacks(List<List<Attack>> unitToAttacks) {
		this.unitToAttacks = unitToAttacks;
	}

	public List<Attack> getAttacksPerUnit() {
		return attacksPerUnit;
	}

	public void setAttacksPerUnit(List<Attack> attacksPerUnit) {
		this.attacksPerUnit = attacksPerUnit;
	}

	public List<Escape> getEscapes() {
		return escapes;
	}

	public void setEscapes(List<Escape> escapes) {
		this.escapes = escapes;
	}
	
	
}
