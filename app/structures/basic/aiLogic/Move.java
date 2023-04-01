package structures.basic.aiLogic;

import java.util.List;

import structures.basic.Tile;
import structures.basic.Unit;
/**
 * This is a supporting class for ai moves, each object represent an move
 * 
 * @author Ying Ting Liu
 */
public class Move {
	
	private Unit unit;
	private List<Tile> positionTiles;
	private Direction direction;
	
	public Move(Unit unit, List<Tile> positionTiles) {
		super();
		this.unit = unit;
		this.positionTiles = positionTiles;
	}

	public Unit getUnit() {
		return unit;
	}

	public List<Tile> getPositionTiles() {
		return positionTiles;
	}	
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Move [moveUnit=").append(unit.getName());
	    for(Tile tile: positionTiles) {
	        sb.append("Tilex: ").append(tile.getTilex());
	        sb.append("Tiley: ").append(tile.getTiley());
	    }
	    sb.append("]");
	    String string = sb.toString();
	    return string;
	}

}
