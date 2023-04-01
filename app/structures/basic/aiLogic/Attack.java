package structures.basic.aiLogic;

import structures.basic.Unit;
/**
 * This is a supporting class for ai action, each object represent an action
 * @author Fong Wei Lam
 * @author Ying Ting Liu
 */
public class Attack {
	
	private Unit attacker;
	private Unit defender;
	private int weight;
	private boolean dieAfterAttack;
	
	public Attack(Unit attacker, Unit defender) {
		this.attacker = attacker;
		this.defender = defender;
		this.weight = 0;
		this.setDieAfterAttack(attacker.getHealth() < defender.getAttack());
	}
	
	public Attack(Unit attacker, Unit defender, int weight) {
		this.attacker = attacker;
		this.defender = defender;
		this.weight = weight;
		this.setDieAfterAttack(attacker.getHealth() < defender.getAttack());
	}

	public Unit getAttacker() {
		return attacker;
	}

	public void setAttacker(Unit attacker) {
		this.attacker = attacker;
	}

	public Unit getDefender() {
		return defender;
	}

	public void setDefender(Unit defender) {
		this.defender = defender;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Attack [attacker=").append(attacker.getName()).append("], [defenders: ").append("\n");
        sb.append("defender name: " + defender.getName() + ",");	        
	    sb.append("\n").append("]");
	    String string = sb.toString();
	    return string;
	}

	public boolean isDieAfterAttack() {
		return dieAfterAttack;
	}

	public void setDieAfterAttack(boolean dieAfterAttack) {
		this.dieAfterAttack = dieAfterAttack;
	}

	

}
