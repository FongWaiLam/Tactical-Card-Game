package structures.basic.ability;

/**
 * This is an enumerate that simply holds the names
 * of the various unit ability types.
 * @author Fong Wai Lam
 */
public enum AbilityType {

	onSummonDrawCard,
	deathDrawCard,
	avatarDamaged,
	spellThief,
	onSummonHeal, //heal a unit once summon
	provoke,
	attackTwice,
	airdrop, // summon anywhere
	flying, // move anywhere
	rangedAttack; // attack any enemy
}

