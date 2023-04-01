package structures.basic;

/**
 * Spell class extends from Card and add spell specialties.
 * 
 * @author Ying Ting Liu
 * @author Fong Wai Lam
 */

public class Spell extends Card{

	int spellHealth = 0;
	int spellAttack = 0;
	boolean spellKillUnit = false;

	public Spell() {
		super();
	}

	public Spell(Card card) {
		super.id = card.getId();
		super.cardname = card.getCardname();
		super.manacost = card.getManacost();
		super.bigCard = card.getBigCard();
		super.miniCard = card.getMiniCard();
	}

	public int getSpellHealth() {
		return spellHealth;
	}

	public void setSpellHealth(int spellHealth) {
		this.spellHealth = spellHealth;
	}

	public int getSpellAttack() {
		return spellAttack;
	}

	public void setSpellAttack(int spellAttack) {
		this.spellAttack = spellAttack;
	}

	public boolean isSpellKillUnit() {
		return spellKillUnit;
	}

	public void setSpellKillUnit(boolean spellKillUnit) {
		this.spellKillUnit = spellKillUnit;
	}
	
}
