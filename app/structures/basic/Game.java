package structures.basic;

import akka.actor.ActorRef;
import controllers.ActionController;
import controllers.CardController;
import controllers.GameController;
import controllers.PlayerController;
import structures.GameState;

/**
 * This class manages the flow of the game and utilizes methods to initialize
 * the game, end the game, and manage turns. Players, boards, and the current
 * game state are added to this class. Additionally, it provides functionality
 * for actions such as attacking, moving, and enforcing basic game rules and
 * restrictions. However, the current state of the game is stored within the
 * GameState object.
 * 
 * @author Ying Ting Liu
 * @author Xuehui-Chen
 * @author Fong Wai Lam
 */

public class Game {
	
	private final CardController cardController;
	private final GameController gameController;
	private final ActionController actionController;
	private final PlayerController playerController;	

	/**
	 * Constructs a new game with initialization and game controller and card controller 
	 * @param out       The actor reference used for output.
	 * @param gameState The game state used for initialization.
	 */
	public Game(ActorRef out, GameState gameState) {
		
		this.cardController = new CardController(out, gameState);
		this.gameController = new GameController(out, gameState);
		this.playerController = new PlayerController(out, gameState);
		
		this.actionController = new ActionController(out, gameState);
		
		gameInit(out, gameState);
	}

	public void gameInit(ActorRef out, GameState gameState) {

		Board.buildBoard(out);		
		
		// Place Avatars on Board
		Avatar.initialiseHumanAvatar(out);
		Avatar.initialiseAIAvatar(out);
		
		// Refresh the memory of the Players due to Singleton
		HumanPlayer.RefreshHumanPlayer();
		AIPlayer.RefreshAIPlayer();

		// Start with HumanPlayer
		gameState.currentPlayer = HumanPlayer.getInstance();
		gameState.waitingPlayer = AIPlayer.getInstance();
		
		// Set players to avatars
		Avatar.avatar1.setPlayer(HumanPlayer.getInstance());
		Avatar.avatar2.setPlayer(AIPlayer.getInstance());
		
		// Show the Health at the initialisation
		HumanPlayer.getInstance().refreshHealth(out);
		AIPlayer.getInstance().refreshHealth(out);
				
		// Draw 3 Cards for Human Player
		for (int i = 0; i < 3 ; i++) {
			HumanPlayer.getInstance().drawCard();
		}		
		
		// Set Mana for Human Player only (Human starts first)
		playerController.gainMana(1);

		// show card on hand
		cardController.showCardOnHand();

	}
	

	public CardController getCardController() {
		return cardController;
	}

	public GameController getGameController() {
		return gameController;
	}

	public PlayerController getPlayerController() {
		return playerController;
	}
	
	public ActionController getActionController() {
		return actionController;
	}
	
}
