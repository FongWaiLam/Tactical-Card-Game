package structures.basic;
import java.util.ArrayList;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.ability.Ability;
import structures.basic.ability.AbilityType;
import structures.basic.ability.AvatarDamagedAbility;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
/**
 * Avatar class extends from the Unit class and creates instances of the human and AI player avatars.
 * The class initializes the avatars and sets their positions, attack, and health.
 * 
 * @author Fong Wai Lam
 * @author YenNing Jia
 */
public class Avatar extends Unit{
	
	public static Avatar avatar1; // Create an human avatar (Load Unit) (unique ID: 40) 	
	public static Avatar avatar2; // Create an AI avatar (Load Unit) (unique ID: 41)
	
	private Player player = null;
	
	public static void initialiseHumanAvatar(ActorRef out) {
		
		avatar1 = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 40, Avatar.class);

		avatar1.setName("Avatar 1");
		
		// Save the x and y position to the unit
		avatar1.setPositionByTile(Board.getBoard()[2][1]);

		
		// Draw the avatar to the tile[Y][X]
		BasicCommands.drawUnit(out, avatar1, Board.getBoard()[2][1]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Set the attack and health
		avatar1.setAttack(2);
		BasicCommands.setUnitAttack(out, avatar1, 2);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		
		avatar1.setHealth(20);
		avatar1.setMaxHealth(20);
		avatar1.setMaxHealth(20);
		BasicCommands.setUnitHealth(out, avatar1, 20);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	public static void initialiseAIAvatar(ActorRef out) {
		
		avatar2 = (Avatar) BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 41, Avatar.class);

		avatar2.setName("Avatar 2");
		
		// Save the x and y position to the unit
		avatar2.setPositionByTile(Board.getBoard()[2][7]);

		
		// Draw the avatar to the tile[Y][X]
		BasicCommands.drawUnit(out, avatar2, Board.getBoard()[2][7]);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		
		// Set the attack and health
		avatar2.setAttack(2);
		BasicCommands.setUnitAttack(out, avatar2, 2);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		
		avatar2.setHealth(20);
		avatar2.setMaxHealth(20);
		BasicCommands.setUnitHealth(out, avatar2, 20);
		try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
		
		
	}

	@Override
	public void beAttacked(ActorRef out,GameState gameState, int attackValue) {
		super.beAttacked(out, gameState, attackValue);
		
		// Set health to player as well
		player.setHealth(getHealth());
		player.refreshHealth(out);
		
		AvatarDamagedAbility avatarDamagedAbility = new AvatarDamagedAbility();
		System.out.println("test if avatar damage?");
		avatarDamagedAbility.perform(out, gameState, player);
		
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
