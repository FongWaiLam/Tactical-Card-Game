import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import structures.basic.ability.*;
import structures.basic.Card;
import structures.basic.Unit;

public class AbilityTest {

	@Test
	public void testHasAbility() {
		// create a unit with some abilities
		Set<AbilityType> abilities = new HashSet<>();
		abilities.add(AbilityType.rangedAttack);

		Unit unit = new Unit();
		unit.setId(2);
		unit.setAbilities();

		// test if the unit has the ranged ability
		assertTrue(Ability.hasAbility(unit, AbilityType.rangedAttack));

		// test if the unit has the flying ability
		assertFalse(Ability.hasAbility(unit, AbilityType.flying));

		// test if the unit has the airdrop ability (which it does not have)
		assertFalse(Ability.hasAbility(unit, AbilityType.airdrop));
	}

	@Test
	public void testHasSummonAbility() {
		// create a card with some summon abilities
		Set<AbilityType> summonAbilities = new HashSet<>();
		summonAbilities.add(AbilityType.onSummonHeal);
        
		Card card = new Card();
		card.setId(5);
		card.setSummonAbilities();
		// test if the card has the onSummonHeal ability
		assertTrue(Ability.hasSummonAbility(card, AbilityType.onSummonHeal));

		// test if the card has the onSummonDrawCard ability
		assertFalse(Ability.hasSummonAbility(card, AbilityType.onSummonDrawCard));

		// test if the card has the airdrop ability (which it does not have)
		assertFalse(Ability.hasSummonAbility(card, AbilityType.airdrop));
	}
}
