package structures.basic;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.ability.Ability;
import structures.basic.ability.AbilityType;
import structures.basic.ability.RangeAttackAbility;
import utils.BasicObjectBuilders;
import utils.ScopeCompute;
import utils.StaticConfFiles;
import utils.UnitAbilityMapper;

/**
 * This is a representation of a Unit on the game board. A unit has a unique id
 * (this is used by the front-end. Each unit has a current UnitAnimationType,
 * e.g. move, or attack. The position is the physical position on the board.
 * UnitAnimationSet contains the underlying information about the animation
 * frames, while ImageCorrection has information for centering the unit on the
 * tile.
 * 
 * @author Dr. Richard McCreadie
 * 
 * @auther JingYi Xiang
 * @author Fong Wei Lam
 * @author Xuehui Chen
 *
 */
public class Unit {

	// Jingyi
	@JsonIgnore
	private String name;
	@JsonIgnore
	private int health;
	@JsonIgnore
	private int attack;
	// Most units can move once and then attack once
	// After attack, cannot move
	@JsonIgnore
	private int moveChance = 1;
	@JsonIgnore
	private int attackChance = 1;
	@JsonIgnore
	private int maxHealth;
	@JsonIgnore
	private Set<AbilityType> abilities = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getMoveChance() {
		return moveChance;
	}

	public void setMoveChance(int moveChance) {
		this.moveChance = moveChance;
	}

	public int getAttackChance() {
		return attackChance;
	}

	public void setAttackChance(int attackChance) {
		this.attackChance = attackChance;
	}

	public Set<AbilityType> getAbilities() {
		return abilities;
	}

	// Call by summonUnit() in GameController
	public void setAbilities() {
		UnitAbilityMapper.mapAbilities(this);
	}

	public void setAbilities(AbilityType ability) {
		this.abilities.add(ability);
	}

	@JsonIgnore
	protected static ObjectMapper mapper = new ObjectMapper(); // Jackson Java Object Serializer, is used to read java
																// objects from a file

	int id;
	UnitAnimationType animation;
	Position position;
	UnitAnimationSet animations;
	ImageCorrection correction;
	
	public Unit() {}
	
	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;

		position = new Position(0, 0, 0, 0);
		this.correction = correction;
		this.animations = animations;
	}

	public Unit(int id, UnitAnimationSet animations, ImageCorrection correction, Tile currentTile) {
		super();
		this.id = id;
		this.animation = UnitAnimationType.idle;

		position = new Position(currentTile.getXpos(), currentTile.getYpos(), currentTile.getTilex(),
				currentTile.getTiley());
		this.correction = correction;
		this.animations = animations;
	}

	public Unit(int id, UnitAnimationType animation, Position position, UnitAnimationSet animations,
			ImageCorrection correction) {
		super();
		this.id = id;
		this.animation = animation;
		this.position = position;
		this.animations = animations;
		this.correction = correction;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UnitAnimationType getAnimation() {
		return animation;
	}

	public void setAnimation(UnitAnimationType animation) {
		this.animation = animation;
	}

	public ImageCorrection getCorrection() {
		return correction;
	}

	public void setCorrection(ImageCorrection correction) {
		this.correction = correction;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public UnitAnimationSet getAnimations() {
		return animations;
	}

	public void setAnimations(UnitAnimationSet animations) {
		this.animations = animations;
	}

	/**
	 * This command sets the position of the Unit to a specified tile.
	 * 
	 * @param tile
	 */
	@JsonIgnore
	public void setPositionByTile(Tile tile) {
		position = new Position(tile.getXpos(), tile.getYpos(), tile.getTilex(), tile.getTiley());
	}

	// FWL 18 Feb 14:42
	public boolean OnTile(int tileX, int tileY) {
		return this.getPosition().equals(tileX, tileY);
	}

	// FWL 18 Feb 14:42
	public boolean OnTile(Tile tile) {
		return this.getPosition().equals(tile.getTilex(), tile.getTiley());
	}

	// FWL 22 Feb 23:23
	public Tile getTile() {
		return Board.getBoard()[position.getTiley()][position.getTilex()];
	}

	// 25 Feb 13:23
	// Overload
	@JsonIgnore
	public void moveTo(ActorRef out, GameState gameState, int tilex, int tiley) {
		// Get Tile to move to
		Tile newTile = Board.getBoard()[tiley][tilex];

		moveTo(out, gameState, newTile);

	}

	// 25 Feb 13:56
	@JsonIgnore
	public void moveTo(ActorRef out, GameState gameState, Tile newTile) {

		// Save the new position of this unit after moving
		int newTilex = newTile.tilex;
		int newTiley = newTile.tiley;

		// Move performed, refresh the highlight
		Board.resetWholeBoard(out);

		int diffTilex = newTilex - position.tilex;
		int diffTiley = newTiley - position.tiley;

		// If movement is diagonal, check the adjacent cardinal direction
		// --> Move via the cardinal direction that has no units.
		if (Math.abs(diffTilex) == 1 && Math.abs(diffTiley) == 1) {

			// Check if vertical tile would have obstacle or not
			Tile verticalTile = Board.getBoard()[newTiley][position.tilex];
			if (!ScopeCompute.getUnitTileSet(gameState, 1).contains(verticalTile)
					&& !ScopeCompute.getUnitTileSet(gameState, 2).contains(verticalTile)) {
				// vertical Tile has not units, so go to diagonal tile via vertical tile
				// Display: This unit move to newTile
				BasicCommands.moveUnitToTile(out, this, newTile, true);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {

				// If vertical Tile has units, so go to diagonal tile via horizontal tile (no
				// true at the end) - BasicCommands.moveUnitToTile(out, this, newTile);
				// Display: This unit move to newTile
				BasicCommands.moveUnitToTile(out, this, newTile);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} else {

			// Display: This unit move to newTile
			BasicCommands.moveUnitToTile(out, this, newTile);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Set unit position
		setPositionByTile(newTile);

		// After moved, moveChance-1, selectedUnit reset null
		this.setMoveChance(this.getMoveChance() - 1);
		gameState.selectedUnit = null;

	}

	// Unit attack an enemy
	@JsonIgnore
	public void attackEnemy(ActorRef out, GameState gameState, Unit enemyUnit) {
		// Attack performed, refresh the highlight
		Board.resetWholeBoard(out);

		// Animation: Attack (attack)
		BasicCommands.playUnitAnimation(out, this, UnitAnimationType.attack);
		try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Story #12 - Unit Action: Ranged Attack
		if (Ability.hasAbility(this, AbilityType.rangedAttack)) {			
			RangeAttackAbility rangeAttackAbility = new RangeAttackAbility(this, enemyUnit);
			rangeAttackAbility.perform(out, gameState);
		}		
		
		// Attack the enemy (Enemy is being attacked)
		enemyUnit.beAttacked(out, gameState, getAttack());
		attackChance--;

		BasicCommands.playUnitAnimation(out, this, UnitAnimationType.idle);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Check if enemy has died or not
		// Died: deleteUnit()
		// Alive: counterAttack this unit
		if (!enemyUnit.unitDeathCheck(out, gameState)) {
			enemyUnit.counterAttack(out, gameState, this);

			// Check if this unit is being killed by counterAttack
			this.unitDeathCheck(out, gameState);

		}
		// Reset to selectedUnit to null
		gameState.selectedUnit = null;
	}

	// 25 Feb 13:16
	@JsonIgnore
	public void beAttacked(ActorRef out, GameState gameState, int attackValue) {

		// Animation: Being attacked (hit)
		BasicCommands.playUnitAnimation(out, this, UnitAnimationType.hit);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Health is decreased by the enemy's attack value
		if ((health - attackValue) >= 0) {
			setHealth(health - attackValue);
		} else {
			setHealth(0);
		}

		// Set unit display health
		BasicCommands.setUnitHealth(out, this, health);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Animation: Standby (idle)
		BasicCommands.playUnitAnimation(out, this, UnitAnimationType.idle);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@JsonIgnore
	// firstAttackUnit - the unit who initiate the first attack
	public void counterAttack(ActorRef out, GameState gameState, Unit firstAttackUnit) {
		// Get the scope for counter-attack
		Set<Tile> validCounterAttackScope = ScopeCompute.counterAttackScope(gameState, this);

		// Perform counterAttack only if the first attack unit is in scope
		if (validCounterAttackScope.contains(firstAttackUnit.getTile())) {
			// Animation: Attack (attack)
			BasicCommands.playUnitAnimation(out, this, UnitAnimationType.attack);
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			
			// Animation: Standby (idle)
			BasicCommands.playUnitAnimation(out, this, UnitAnimationType.idle);
			try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
			
			// firstAttackUnit is being counter-attacked.
			firstAttackUnit.beAttacked(out, gameState, getAttack());
		}
		
	}

	@JsonIgnore
	public boolean unitDeathCheck(ActorRef out, GameState gameState) {

		// Windshrike = 0, its owner draws a card
		if (health <= 0) {
			
			if(Ability.hasAbility(this, AbilityType.deathDrawCard)) {
				gameState.currentPlayer.drawCard();
			}

			BasicCommands.addPlayer1Notification(out, "drop to zero", 2);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Animation: Death (death)
			BasicCommands.playUnitAnimation(out, this, UnitAnimationType.death);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Delete Unit from display
			BasicCommands.deleteUnit(out, this);

			// Delete Unit from Board
			if (!gameState.currentPlayer.getUnitsOnBoard().remove(this)) {
				gameState.waitingPlayer.getUnitsOnBoard().remove(this);
			}

			return true;
		}
		return false;
	}

	// 25 Feb 13:16
	@JsonIgnore
	public void beHealed(ActorRef out, int healValue) {

		// Health is increased by spell
		if ((health + healValue) > maxHealth) {
			setHealth(maxHealth);
			BasicCommands.addPlayer1Notification(out, "exceed max health", 2);
		} else {
			setHealth(health + healValue);
		}

		// Set unit display health
		BasicCommands.setUnitHealth(out, this, health);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void strengthenAttack(ActorRef out, int gainValue) {

		// Attack is increased by spell
		setAttack(getAttack() + gainValue);

		// Set unit display health
		BasicCommands.setUnitAttack(out, this, getAttack());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		BasicCommands.addPlayer1Notification(out, this.name + " is now strengthen!", 2);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
