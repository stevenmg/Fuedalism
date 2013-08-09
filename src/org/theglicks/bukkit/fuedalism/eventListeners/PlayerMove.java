package org.theglicks.bukkit.fuedalism.eventListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.theglicks.bukkit.fuedalism.landManagement.Fief;
import org.theglicks.bukkit.fuedalism.landManagement.KingdomLandClaim;

public class PlayerMove implements Listener{
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		//Make sure the player has moved to another block
		int fromX = (int) event.getFrom().getX();
		int fromZ = (int) event.getFrom().getZ();
		int toX = (int) event.getTo().getX();
		int toZ = (int) event.getTo().getZ();
		
		if(fromX!=toX || fromZ!=toZ){
			//Checks if player is entering a kingdom claim
			KingdomLandClaim claim = new KingdomLandClaim(event.getTo());
			if (claim.exists()){
				KingdomLandClaim claim1 = new KingdomLandClaim(event.getFrom());
				if(!claim1.exists()){
					event.getPlayer().sendMessage("You are now in " + claim.getKingdom().getName() + "'s claim");
				}
			}
		
			//Checks if player is entering a fief
			Fief f = new Fief(event.getTo());
			if(f.exists()){
				Fief f1 = new Fief(event.getFrom());
				if(!f1.exists()){
					event.getPlayer().sendMessage("You are now in " + f.getOwner() + "'s fief");
				}
			}
		}
	}
}