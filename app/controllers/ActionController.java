package controllers;

import java.util.Set;
import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.ability.Ability;
import structures.basic.ability.AbilityType;
import utils.HighlightDisplay;
import utils.ScopeCompute;

/**
 * This class controls all the action including moves, attack and their corresponding scope.
 * This class provide reusable code for ai and human player.
 * 
 * @author Fong Wai Lam
 * @author JingYi Xiang
 */
public class ActionController {

	private final ActorRef out;
	private final GameState gameState;

	Unit selectedUnit;
	Unit enemyUnit;

	// Tiles that friendly unit can move to
	Set<Tile> validMoveScope = null;

	// Tiles that friendly unit can attack to (Where enemy on)
	Set<Tile> validAttackScope = null;

	// Tiles that friendly unit can attack without moving
	Set<Tile> validNoMoveAttackScope = null;

	// When attack, friendly unit should move to
	Set<Tile> validAttackMoveScope = null;


	public ActionController(ActorRef out, GameState gameState) {
		this.out = out;
		this.gameState = gameState;
	}

	// Save the unit as selectedUnit in GameState, before refreshing scopes
	public void refreshScopes(int tilex, int tiley) {
		// Get new unit on Tile (Without saving to GameState)
		Unit unitOnTile = gameState.currentPlayer.unitOnTile(tilex, tiley);

		if (unitOnTile.getAttackChance() > 0) {

			if (gameState.selectedUnit != gameState.currentPlayer.unitOnTile(tilex, tiley)) {
				// Store the newly selected unit, only if this unit has attackChance and
				// moveChance
				gameState.selectedUnit = unitOnTile;

			}
		}
		refreshScopes(unitOnTile);
	}

	public void refreshScopes(Unit unitOnTile) {

		validMoveScope = null;
		validAttackScope = null;
		validNoMoveAttackScope = null;
		validAttackMoveScope = null;

		// Retrieve original tilex and tiley of selected unit
		int tilex = unitOnTile.getPosition().getTilex();
		int tiley = unitOnTile.getPosition().getTiley();

		// (2.2) Check unit status (attackChance, moveChance)
		if (unitOnTile.getAttackChance() > 0) {

			// Story # 27 - Unit Ability: Provoke
			Set<Tile> provokeScope = ScopeCompute.checkProvoked(gameState, tilex, tiley);

			// If adjacent enemy has provoke, use the provoke scope
			if (provokeScope.size() != 0) {
				validMoveScope = null;
				validAttackScope = provokeScope;
			} else {

				if (unitOnTile.getMoveChance() > 0) {

					// Story #30 - Unit Ability: Flying
					if (Ability.hasAbility(unitOnTile, AbilityType.flying) && unitOnTile.getMoveChance() > 0) {

						validMoveScope = ScopeCompute.rangedSummonMoveScope(gameState);
						validAttackScope = ScopeCompute.getUnitTileSet(gameState, 2); // SetNo. = 2 (enemy)

					// Story #12 - Unit Action: Ranged Attack
					} else if (Ability.hasAbility(unitOnTile, AbilityType.rangedAttack)) {

						validMoveScope = ScopeCompute.unitNotMoveNotAttack(gameState, tilex, tiley).get(0);
						validAttackScope = ScopeCompute.getUnitTileSet(gameState, 2);

					} else {
						
						validMoveScope = ScopeCompute.unitNotMoveNotAttack(gameState, tilex, tiley).get(0);
						validAttackScope = ScopeCompute.unitNotMoveNotAttack(gameState, tilex, tiley).get(1);

					}

					validNoMoveAttackScope = ScopeCompute.unitNotMoveNotAttack(gameState, tilex, tiley).get(2);

				} else if (unitOnTile.getMoveChance() <= 0) {

					// Story #12 - Unit Action: Ranged Attack
					if (Ability.hasAbility(unitOnTile, AbilityType.rangedAttack)) {

						validAttackScope = ScopeCompute.getUnitTileSet(gameState, 2);

					} else {

						validAttackScope = ScopeCompute.unitMovedCanAttack(gameState, tilex, tiley);

					}
				}
			}
		}
	}

	// Click on friendly unit
	public void selectFriendlyUnitForAction(int tilex, int tiley) {
		// A new unit is clicked, refresh the highlight
		Board.resetWholeBoard(out);

		refreshScopes(tilex, tiley);

		if (validMoveScope != null) {
			// White Highlight
			HighlightDisplay.drawSetHighlight(out, validMoveScope, 1);
		}
		if (validAttackScope != null) {
			// Red Highlight
			HighlightDisplay.drawSetHighlight(out, validAttackScope, 2);
		}

	}

	// Click on an empty tile
	public void selectTileToMove(Unit selectedUnit, int tilex, int tiley) {

		if (selectedUnit.getMoveChance() > 0) {

			refreshScopes(selectedUnit);

			// check if clickedTile is in validMoveScope
			if (validMoveScope.contains(Board.getBoard()[tiley][tilex])) {

				selectedUnit.moveTo(out, gameState, tilex, tiley);
			}
		}

	}

	// Click on an enemy unit
	public void selectEnemyToAttack(Unit selectedUnit, Unit enemyUnit) {

		refreshScopes(selectedUnit);

		// Check if enemy within attack scope
		if (validAttackScope.contains(enemyUnit.getTile())) {

			if (validMoveScope != null) {
				validAttackMoveScope = ScopeCompute.attackEnemyMoveScope(gameState, enemyUnit, validMoveScope);

				// Move only if the current tile cannot attack
				if ((validNoMoveAttackScope == null || !validNoMoveAttackScope.contains(enemyUnit.getTile())) &&
						!Ability.hasAbility(selectedUnit, AbilityType.rangedAttack)) {
					Tile moveTile = null;

					// Decide which tile to move to if scope > 1
					if (validAttackMoveScope.size() > 1) {

						// Loop each tile to find the nearest tile to the enemy
						for (Tile testMoveTile : validAttackMoveScope) {

							// Compare each tile to the unit's original tile
							int unitOriginalTilex = selectedUnit.getPosition().getTilex();
							int unitOriginalTiley = selectedUnit.getPosition().getTiley();

							int testMoveTilex = testMoveTile.getTilex();
							int testMoveTiley = testMoveTile.getTiley();
							int diffTilex = Math.abs(testMoveTilex - unitOriginalTilex);
							int diffTiley = Math.abs(testMoveTiley - unitOriginalTiley);

							// If equals to 1, it is a cardinal movement.(Good) 
							// If equals to 2, it is a diagonal movement (Undesirable)
							if (diffTilex + diffTiley == 1) {
								moveTile = testMoveTile;
								break;
							}
						}

						// If scope.size() == 1, move to this tile directly
					} else {
						if (validAttackMoveScope != null) {
							moveTile = validAttackMoveScope.iterator().next();
						}
					}
					// Move to the tile that can attack
					if (moveTile != null) {
						selectedUnit.moveTo(out, gameState, moveTile);
					}
				}
				// enemyUnit in validNoMoveAttackScope, proceed to the attack directly
			}

			// Unit cannot move, only attack
			// Attack the enemy
			selectedUnit.attackEnemy(out, gameState, enemyUnit);
		}
	}

	public void unitAction(int tilex, int tiley) {

		// Click on a friendly Unit
		if (gameState.currentPlayer.unitOnTile(tilex, tiley) != null) {

			selectFriendlyUnitForAction(tilex, tiley);

			// Clicked on an empty tile --> Player wants to move only
		} else if (gameState.currentPlayer.unitOnTile(tilex, tiley) == null
				&& gameState.waitingPlayer.unitOnTile(tilex, tiley) == null && gameState.selectedUnit != null) {

			selectedUnit = gameState.selectedUnit;

			selectTileToMove(selectedUnit, tilex, tiley);

			// Clicked on an enemy --> Unit will move and then attack
		} else if (gameState.waitingPlayer.unitOnTile(tilex, tiley) != null && gameState.selectedUnit != null) {

			selectedUnit = gameState.selectedUnit;

			enemyUnit = gameState.waitingPlayer.unitOnTile(tilex, tiley);

			selectEnemyToAttack(selectedUnit, enemyUnit);

		}
	}

	public Set<Tile> getValidMoveScope(Unit unit) {
		refreshScopes(unit);
		return validMoveScope;
	}

	public Set<Tile> getValidAttackScope(Unit unit) {
		refreshScopes(unit);
		return validAttackScope;
	}

}
