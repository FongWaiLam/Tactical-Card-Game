package utils;

import structures.basic.Unit;
import structures.basic.ability.AbilityType;

/**
 * This class map the ability with units
 * 
 * @author Fong Wei Lam
 */
public class UnitAbilityMapper {

	public static void mapAbilities(Unit unit) {
		AbilityType abilityType1 = null;
		AbilityType abilityType2 = null;

		switch (unit.getId()) {

			// Pureblade Enforcer
			case 1:
			case 13:
				abilityType1 = AbilityType.spellThief;
				break;
			// Azure Herald
			case 5:
			case 15:
				abilityType1 = AbilityType.onSummonHeal;
				break;
			// Silverguard Knight
			case 3:
			case 10:
				abilityType1 = AbilityType.provoke;
				abilityType2 = AbilityType.avatarDamaged;
				break;
			// Azurite Lion
			case 7:
			case 17:
				abilityType1 = AbilityType.attackTwice;
				break;
			// Fire Spitter
			case 2:
			case 11:
				abilityType1 = AbilityType.rangedAttack;
				break;
			// Ironcliff Guardian
			case 6:
			case 16:
//				abilityType1 = AbilityType.airdrop;
				abilityType2 = AbilityType.provoke;
				break;
//		// Planar Scout
		//		case 28: case 38:
		//		abilityType1 = AbilityType.airdrop;
		//		break;
			// Rock Pulveriser
			case 20:
			case 30:
				abilityType1 = AbilityType.provoke;
				break;
			// Pyromancer
			case 25:
			case 35:
				abilityType1 = AbilityType.rangedAttack;
				break;
//		// Blaze Hound
		//		case 23: case 33:
		//		abilityType1 = AbilityType.onSummonDrawCard;
		//		break;
			// Windshrike
			case 24:
			case 34:
				abilityType1 = AbilityType.flying;
				abilityType2 = AbilityType.deathDrawCard;
				break;
			// Serpenti
			case 26:
			case 36:
				abilityType1 = AbilityType.attackTwice;
				break;
		}

		// Assign corresponding abilities
		if (abilityType1 != null) {
			unit.setAbilities(abilityType1);
		}
		if (abilityType2 != null) {
			unit.setAbilities(abilityType2);
		}

	}

}
