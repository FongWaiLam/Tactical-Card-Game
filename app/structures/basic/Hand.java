package structures.basic;

import java.util.List;
/**
 * This class represent the card on hand, 
 * CardController and PlayerController use this class to drqw cards and initialization. 
 * 
 * @author Ying Ting Liu
 * @author Fong Wai Lam  
 */
public class Hand {
	
	private Card[] handOfCards;
	
	private final int max = 6;

	
	public Hand() {
		handOfCards = new Card[6];
    }
	
	public Card getCard(int handPosition) {
		return handOfCards[handPosition - 1];
	}

	public void remove(int handPosition) {
		if ((handPosition - 1) >= 0 && (handPosition - 1) < handOfCards.length) {
			handOfCards[handPosition - 1] = null;
		}		
	}
	
	public void remove(Card card) {
	    for (int i = 0; i < handOfCards.length; i++) {
	        if (handOfCards[i] != null && handOfCards[i].equals(card)) {
	            handOfCards[i] = null;
	            return; // Exit the method after removing the card
	        }
	    }
	}
	
	public boolean isFull() {
		for (int i = 0; i < max; i++) {			
	        if (handOfCards[i] == null) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public int getFirstEmptyIndex() {
	    if (handOfCards == null) {
	    	handOfCards = new Card[6];
	        return 0; 
	    }
	    int index = 0;
	    for (Card card : handOfCards) {
	        if (card == null) {
	            return index;
	        }
	        index++;
	    }
	    return -1; // not found return -1
	}
	
	public boolean drawCardFromDeck(List<Card> deck) {
	    if (!deck.isEmpty()) {	    	
	    	int index = this.getFirstEmptyIndex();	    	
	        Card drawnCard = deck.get(0);

	        if(!this.isFull()) {
	        	handOfCards[index] = drawnCard;
	        	handOfCards[index].setSummonAbilities();
	        	
	        	System.out.println(handOfCards[index].getCardname() + " ability: " + handOfCards[index].getSummonAbilities());
	        }	        
	        deck.remove(0);
	        
	        return true;
	        
	    } else {
	    	
	    	return false;
	    	
	    }
	}


	public Card[] getHandOfCards() {
		return handOfCards;
	}

	public void setHandOfCards(Card[] handOfCards) {
		this.handOfCards = handOfCards;
	}

	public boolean isEmpty() {
		for (int i = 0; i < max; i++) {			
	        if (handOfCards[i] != null) {
	            return false;
	        }
	    }
	    return true;
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder("Hand [");
	    
	    for (int i = 0; i < handOfCards.length; i++) {
	        if (handOfCards[i] != null) {
	            sb.append("handOfCard ").append(i).append("=")
	              .append(handOfCards[i].getCardname()).append(" ");
	        }
	    }
	    
	    sb.append("]");
	    return sb.toString();
	}	
	
	
}
