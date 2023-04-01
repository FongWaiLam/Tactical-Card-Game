package utils;


import structures.basic.Card;
import structures.basic.ability.AbilityType;
/**
 * This class map the summon ability with cards
 * @author Fong Wei Lam
 */
public class SummonAbilityMapper {
	
	public static void mapAbilities(Card card) {
		AbilityType summonAbility = null;
		
		switch(card.getId()) {
		
		// Azure Herald
		case 5: case 15:
			summonAbility = AbilityType.onSummonHeal;
		break;

		// Ironcliff Guardian
		case 6: case 16:
			summonAbility = AbilityType.airdrop;
		break;
		
		// Planar Scout
		case 28: case 38:
			summonAbility = AbilityType.airdrop;
		break;
		
		// Blaze Hound
		case 23: case 33:
			summonAbility = AbilityType.onSummonDrawCard;
		break;
		}
		// Assign corresponding abilities

		card.setSummonAbilities(summonAbility);
	}
}
