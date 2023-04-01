package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Card;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.ability.Ability;
import structures.basic.ability.AbilityType;
/**
 * This class calculate the scope, supports other classes
 * @author Fong Wei Lam
 */
public class ScopeCompute {

	// Get the set of tiles of units
	// Set No. 1 - Friendly Units (Current Player) ; Set No. 2 - Enemy Units (Enemy Player)
	public static Set<Tile> getUnitTileSet(GameState gameState, int setNo) {
		ArrayList<Unit> units = null;
		// Get relevant units
		if( setNo == 1) {
			// Friendly Units
			 units = gameState.currentPlayer.getUnitsOnBoard();
		} else if (setNo == 2) {
			// Enemy Units
			units = gameState.waitingPlayer.getUnitsOnBoard();
		}
		
		Set<Tile> tileSet = new HashSet<>();
		// Add tile of each unit
		for (Unit unit: units) {
			tileSet.add(unit.getTile());
		}
		return tileSet;
	}
	
	
	
	// Story # 29 - Airdrop : Ironcliff Guardian (HumanPlayer), Planar Scout (AIPlayer)
	// Story # 30 - Flying : Windshrike (AIPlayer)
	public static Set<Tile> rangedSummonMoveScope(GameState gameState) {
		Set<Tile> rangedSummonMoveScope = new HashSet<Tile>();
		
		// Get Full set of tiles
		for (Tile[] tiles: Board.getBoard()) {
			for (Tile tile : tiles) {
				rangedSummonMoveScope.add(tile);
			}
		}
	
		// Remove the tiles occupied by units
		rangedSummonMoveScope.removeAll(getUnitTileSet(gameState, 1));
		rangedSummonMoveScope.removeAll(getUnitTileSet(gameState, 2));
		
		return rangedSummonMoveScope;
		
	}
	
	// Story # 27 - Provoke : Silverguard Knight (HumanPlayer), Ironcliff Guardian (HumanPlayer), Rock Pulveriser (AIPlayer)
	public static Set<Tile> checkProvoked(GameState gameState, int x, int y) {
		Set<Tile> provokedScope = new HashSet<>();
		
		Set<Tile> unitMovedCanAttackScope = unitMovedCanAttack(gameState, x, y);
		
		for (Tile tile : unitMovedCanAttackScope) {
			for (Unit unit : gameState.waitingPlayer.getUnitsOnBoard()) {
				if (unit.OnTile(tile) && Ability.hasAbility(unit, AbilityType.provoke)) {
					provokedScope.add(tile);
					break;
				}
			}
		}
		return provokedScope;
	}
	
	
	// FWL 18 Feb 19:05
	public static Set<Tile> roundScopeCompute(GameState gameState, Tile centralTile, int loop, int maxloop, int[][] scopes, boolean includeEnemy, boolean includeFriend) {
		
		// Get the tile set of the units for friendly or enemy units
		Set<Tile> friendlyTiles = getUnitTileSet(gameState, 1);
		Set<Tile> enemyTiles = getUnitTileSet(gameState, 2);
		
		// Return an array of tiles to be highlighted (HashSet - no repeated coordinates)
		Set<Tile> tilesHighlight = new HashSet<>();
		
		if (centralTile.getTiley() < 0 || centralTile.getTiley() >= 5 || centralTile.getTilex() < 0 || centralTile.getTilex() >= 9) {
			return null;
		}
		if (loop == maxloop) {
			return null;
		}
		
		// Traverse to all directions	
		int newY;
		int newX;
		for (int i = 0; i < scopes.length; i++) {
			newY = centralTile.getTiley() + scopes[i][0];
			newX = centralTile.getTilex() + scopes[i][1];
	
			if (newY >= 0 && newY < 5 && newX >= 0 && newX < 9) {
				Tile newCentralTile = Board.getBoard()[newY][newX];
				
				// No enemy units are on this new central tile
				if (!enemyTiles.contains(newCentralTile) || includeEnemy) {
					
					// Add this tile to the set
					tilesHighlight.add(newCentralTile);
					
					// Next Loop
					Set<Tile> tempTileSet = roundScopeCompute(gameState, newCentralTile, loop + 1, maxloop, scopes, includeEnemy, includeFriend);
					
					// Add tileSet from the next loop
					if (tempTileSet != null) {
						tilesHighlight.addAll(tempTileSet);
					}
				}
			}
		}
		if (loop == 0) {
			tilesHighlight.remove(centralTile);
			if (!includeFriend) {
				tilesHighlight.removeAll(friendlyTiles);
			}
		}
		return tilesHighlight;
	}

	// FWL 19 Feb 20:25
	public static Set<Tile> enemyTileInScope(GameState gameState, Set<Tile> scopeRedHighlight) {

		Set<Tile> enemyTileInScope = new HashSet<>();	
		
		Set<Tile> enemyTileSet = getUnitTileSet(gameState, 2);
		
		// Check if enemy unit in attack Scope
		for (Tile tile: scopeRedHighlight) {
			if (enemyTileSet.contains(tile)) {
				enemyTileInScope.add(tile);
			}
		}
		return enemyTileInScope;
	}

	// Story #6
	// Condition: Unit has not moved or attacked before
	public static List<Set<Tile>> unitNotMoveNotAttack(GameState gameState, int x, int y) {
		List<Set<Tile>> validMoveAttackScope = new ArrayList<>(3);
		Set<Tile> validMoveScope = null;
		Set<Tile> validAttackScope = new HashSet<>();
		Set<Tile> validNoNeedMoveAttackScope = null;
		
		// Create the coordinate for tile clicked
		Tile clickedTile = Board.getBoard()[y][x];
		
		// White Highlight Scope
		int[][] scopes = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };	
		
		// White Highlight Scope without units
				// maxloop means the times of recursion (Default = 2)
				// includeEnemy means whether to include tiles with enemy (exclude EnemyTiles and FriendlyTiles)
		validMoveScope = roundScopeCompute(gameState, clickedTile, 0, 2, scopes, false, false);	
		
		
		// Compute Possible Red Scope (Check if each move tile has any enemy units)
		for (Tile tile : validMoveScope) {
			validAttackScope.addAll(unitMovedCanAttack(gameState, tile.getTilex(), tile.getTiley()));
		}
		// Compute Possible Red Scope if not move
		validAttackScope.addAll(unitMovedCanAttack(gameState, x, y));
		
		// Compute Possible No Need to Move Attack Scope = unitMovedCanAttack scope
		validNoNeedMoveAttackScope = unitMovedCanAttack(gameState, x, y);

		validMoveAttackScope.add(validMoveScope);
		validMoveAttackScope.add(validAttackScope);
		validMoveAttackScope.add(validNoNeedMoveAttackScope);
		return validMoveAttackScope;
	}
	
	// Ying Ting added not sure if it works!
	public static Set<Tile> unitMove(GameState gameState, int x, int y) {
		
		Tile clickedTile = Board.getBoard()[y][x];		
		int[][] scopes = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };	
		
		Set<Tile> validMoveScope = roundScopeCompute(gameState, clickedTile, 0, 2, scopes, false, false);	
		
		return validMoveScope;
	}
	
	// Story #7
	// Unit has no more moves but can attack, highlight attack
	public static Set<Tile> unitMovedCanAttack(GameState gameState, int x, int y) {
		// Create the coordinate for tile clicked
		Tile clickedTile = Board.getBoard()[y][x];

		// Red Highlight Scope
		int[][] scopes = {{ -1, 0 }, {-1, 1 }, { 0, 1 }, {1, 1 }, { 1, 0 }, {1, -1 }, { 0, -1 } , {-1, -1 }};	
		
		// Compute Possible Red Highlight Scope (Check if enemy units in these tiles)
				// includeEnemy means whether to include tiles with enemy (True) - Get Full Scope
		Set<Tile> scopeRedHighlight = roundScopeCompute(gameState, clickedTile, 0, 1, scopes, true, false);

		
		scopeRedHighlight.remove(clickedTile);
		
		// Highlight the tiles with enemyUnits in scope
		Set<Tile> validAttackScope = enemyTileInScope(gameState, scopeRedHighlight);
		
		return validAttackScope;
		
	}


	// FWL 19 Feb 02:57
	// Story# 22
	// PlayCard (When it is an unit Card)
	public static Set<Tile> summon(GameState gameState, Card card) {
		Set<Tile> validSummonScope = new HashSet<>();
		
		// Story # 29 - Unit Ability: Airdrop
		if (Ability.hasSummonAbility(card, AbilityType.airdrop)) {
			
			// Compute Possible White Highlight Scope (Check if enemy units in these tiles)
			validSummonScope = rangedSummonMoveScope(gameState);
			
		} else {
			
			// White Highlight Scope
			int[][] scopes = {{ -1, 0 }, {-1, 1 }, { 0, 1 }, {1, 1 }, { 1, 0 }, {1, -1 }, { 0, -1 } , {-1, -1 }};	
			
			// Get the tile set of friendly units
			Set<Tile> friendlyTileSet = getUnitTileSet(gameState, 1);
					
			for (Tile tile : friendlyTileSet) {
				// Get summon highlight scope of each friendly unit
						// includeEnemy means whether to include tiles with enemy (False - exclude EnemyTiles and FriendlyTiles)
				validSummonScope.addAll(roundScopeCompute(gameState, tile, 0, 1, scopes, false, false));
			}
		}
		return validSummonScope;
		
	}
	
	// FWL 25 Feb 13:31
	public static Set<Tile> attackEnemyMoveScope(GameState gameState, Unit enemyUnit, Set<Tile> validMoveScope) {
		
		// Get tile the enemy is on
		Tile enemyTile = enemyUnit.getTile();
		
		// enemy being attacked Scope
		int[][] scopes = {{ -1, 0 }, {-1, 1 }, { 0, 1 }, {1, 1 }, { 1, 0 }, {1, -1 }, { 0, -1 } , {-1, -1 }};	
				// includeEnemy means whether to include tiles with enemy (False - exclude EnemyTiles)
		Set<Tile> enemyBeingAttackedScope = roundScopeCompute(gameState, enemyTile, 0, 1, scopes, false, false);
		
		// Where friendly unit should move to perform the attack?
			// Find the intersection of validMoveScope and enemyBingAttackScope --> intersection saved in validMoveScope
		validMoveScope.retainAll(enemyBeingAttackedScope);

		return validMoveScope;
	}
	
	public static Set<Tile> counterAttackScope(GameState gameState, Unit unit) {
		// Only can attack adjacent units
		int[][] scopes = {{ -1, 0 }, {-1, 1 }, { 0, 1 }, {1, 1 }, { 1, 0 }, {1, -1 }, { 0, -1 } , {-1, -1 }};
		Set<Tile> validCounterAttackScope = roundScopeCompute(gameState, unit.getTile(), 0, 1, scopes, false, true);
		
		return validCounterAttackScope;
	}
	
	
	public static Set<Tile> spell(ActorRef out, GameState gameState, Card card) {
		
		int cardID = card.getId();
		// Get UnitsOnBoard for validation
		Set<Tile> friendlyTiles = ScopeCompute.getUnitTileSet(gameState, 1);
		Set<Tile> enemyTiles = ScopeCompute.getUnitTileSet(gameState, 2);
		
		// Highlight according to different spells
		switch (cardID) {
			// Player 1 - Truestrike - Deal 2 damage to an enemy unit
			// Applicable to: enemyTiles
		case 4 :
		case 14 :
			return enemyTiles;
			
			// Player 1 - Sundrop Elixir - Add +5 health to a Unit. Not take a unit over its starting health
			// Applicable to: friendlyTiles
		case 8 :
		case 18 :
			// White Highlight Valid Tiles for Heal
			return friendlyTiles;
				
			// Player 2 - Staff of Y’Kir - Add +2 attack to your avatar
			// Applicable to: Current player's avatar
		case 22 :
		case 32 :
			Set<Tile> currentAvatarTile = new HashSet<>();
			currentAvatarTile.add(gameState.currentPlayer.getUnitsOnBoard().get(0).getTile());
			return currentAvatarTile;
			
			// Player 2 - Entropic Decay - Reduce a non-avatar unit to 0 health
			// Applicable to: All enemy units, except enemy's avatar(index = 0)
		case 27 :
		case 37 :
			// Scope for Entropic Decay: enemyUnitsExcludeAvatar
			Tile enemyAvatarTile = gameState.waitingPlayer.getUnitsOnBoard().get(0).getTile();
			Set<Tile> enemyUnitsExcludeAvatar = new HashSet<>(enemyTiles);
			enemyUnitsExcludeAvatar.remove(enemyAvatarTile);
			return enemyUnitsExcludeAvatar;
		}
		return null;	
	}
}