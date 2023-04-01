package structures.basic;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import structures.basic.ability.AbilityType;
import utils.SummonAbilityMapper;

/**
 * This is the base representation of a Card which is rendered in the player's hand.
 * A card has an id, a name (cardname) and a manacost. A card then has a large and mini
 * version. The mini version is what is rendered at the bottom of the screen. The big
 * version is what is rendered when the player clicks on a card in their hand.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Card {
	
	int id;
	
	String cardname;
	int manacost;
	
	MiniCard miniCard;
	BigCard bigCard;
	@JsonIgnore
	private Set<AbilityType> summonAbilities = new HashSet<>();
	
	public Card() {};
	
	public Card(int id, String cardname, int manacost, MiniCard miniCard, BigCard bigCard) {
		super();
		this.id = id;
		this.cardname = cardname;
		this.manacost = manacost;
		this.miniCard = miniCard;
		this.bigCard = bigCard;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCardname() {
		return cardname;
	}
	public void setCardname(String cardname) {
		this.cardname = cardname;
	}
	public int getManacost() {
		return manacost;
	}
	public void setManacost(int manacost) {
		this.manacost = manacost;
	}
	public MiniCard getMiniCard() {
		return miniCard;
	}
	public void setMiniCard(MiniCard miniCard) {
		this.miniCard = miniCard;
	}
	public BigCard getBigCard() {
		return bigCard;
	}
	public void setBigCard(BigCard bigCard) {
		this.bigCard = bigCard;
	}

	// FWL added on 18 Feb 22:02 (Get StaticConfFiles Directly)
	// conf/gameconfs/units/name_name.json
	public String cardStaticConfFile() {
		return "conf/gameconfs/units/" + cardname.toLowerCase().replace(' ', '_') + ".json";
	}

	public Set<AbilityType> getSummonAbilities() {
		return summonAbilities;
	}

	// FWL added on 6 Mar 18:44
	// Set summon abilities in draw card
	public void setSummonAbilities() {
		SummonAbilityMapper.mapAbilities(this);
		
	}
	
	// FWL added on 6 Mar 18:44
	public void setSummonAbilities(AbilityType ability) {
		summonAbilities.add(ability);
		
	}
	
	
}
