package utils;

import java.util.ArrayList;
import java.util.List;

import structures.basic.Card;
import structures.basic.Spell;

/**
 * This is a utility class that provides methods for loading the decks for each
 * player, as the deck ordering is fixed. 
 * @author Richard
 *
 */
public class OrderedCardLoader {

	/**
	 * Returns all of the cards in the human player's deck in order
	 * @return
	 */
	public static List<Card> getPlayer1Cards() {
	
		List<Card> cardsInDeck = new ArrayList<Card>(20);

		
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger, 0, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pureblade_enforcer, 1, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter, 2, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_silverguard_knight, 3, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_truestrike, 4, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azure_herald, 5, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian, 6, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azurite_lion, 7, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_sundrop_elixir, 8, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 9, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_silverguard_knight, 10, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_fire_spitter, 11, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_comodo_charger, 12, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pureblade_enforcer, 13, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_truestrike, 14, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azure_herald, 15, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_ironcliff_guardian, 16, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_azurite_lion, 17, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_sundrop_elixir, 18, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 19, Card.class));
		
		return cardsInDeck;
	}
	
	
	/**
	 * Returns all of the cards in the human player's deck in order
	 * @return
	 */
	public static List<Card> getPlayer2Cards() {
	
		List<Card> cardsInDeck = new ArrayList<Card>(20);
		
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_rock_pulveriser, 20, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_bloodshard_golem, 21, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_staff_of_ykir, 22, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_blaze_hound, 23, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_windshrike, 24, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pyromancer, 25, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_serpenti, 26, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_entropic_decay, 27, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_planar_scout, 28, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 29, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_rock_pulveriser, 30, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_bloodshard_golem, 31, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_staff_of_ykir, 32, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_blaze_hound, 33, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_windshrike, 34, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_pyromancer, 35, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_serpenti, 36, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_entropic_decay, 37, Spell.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_planar_scout, 38, Card.class));
		cardsInDeck.add(BasicObjectBuilders.loadCard(StaticConfFiles.c_hailstone_golem, 39, Card.class));
		
		return cardsInDeck;
	}
	
	public static List<Card> spellCardMapping(List<Card> deck) {		
		for(Card card : deck) {
			if(card instanceof Spell) {
				int cardId = card.getId();
				switch(cardId) {
					case 4: case 14:
						//((Spell) card).setSpellEnemy(true);
						((Spell) card).setSpellHealth(-2);						
						break;
					case 8: case 18:
						((Spell) card).setSpellHealth(5);
						//((Spell) card).setHeathCap(true);;
						break;
					case 22: case 32:
						//((Spell) card).setSpellAvatar(true);	
						((Spell) card).setSpellAttack(2);
						break;
					case 27: case 37:
						((Spell) card).setSpellKillUnit(true);
						break;
				}				
			} 
		}
		return deck;
	}
	
}
