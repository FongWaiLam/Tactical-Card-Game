package structures.basic.aiLogic;

import structures.basic.Tile;
/**
 * This is a supporting class for AI escape actions
 * In order to escape from attack
 * 
 * @author Fong Wei Lam
 * 
 */
public class Escape implements Comparable<Escape>{
	
	

	private Tile tile;
	private int surroundingEnemyNo;




	public Escape(Tile tile, int surroundingEnemyNo) {
		super();
		this.tile = tile;
		this.surroundingEnemyNo = surroundingEnemyNo;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public int getSurroundingEnemyNo() {
		return surroundingEnemyNo;
	}

	public void setSurroundingEnemyNo(int surroundingEnemyNo) {
		this.surroundingEnemyNo = surroundingEnemyNo;
	}

	@Override
	public int compareTo(Escape other) {
		if (this.surroundingEnemyNo > other.getSurroundingEnemyNo()) {
			return 1;
		} else if (this.surroundingEnemyNo < other.getSurroundingEnemyNo()){
			return -1;
		} else {
			return 0;
		}
	}

	
}
