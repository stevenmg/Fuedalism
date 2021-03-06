package org.theglicks.bukkit.governance.eventListeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.theglicks.bukkit.governance.Governance;
import org.theglicks.bukkit.governance.Messages;
import org.theglicks.bukkit.governance.Vassal;
import org.theglicks.bukkit.governance.landManagement.Fief;
import org.theglicks.bukkit.governance.landManagement.KingdomLandClaim;

public class CommandPreprocess implements Listener{
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event){
		String[] cmd = event.getMessage().split(" ");
		cmd[0] = cmd[0].replaceFirst("/", "");
		Vassal v = new Vassal(event.getPlayer().getName());
		
		if(Governance.mainConfig.getConfig().getList("CommandBlocking.blockedInEnemyClaim").contains(cmd[0])){	
			KingdomLandClaim c = new KingdomLandClaim(v.getPlayer().getLocation());
			Fief f = new Fief(v.getPlayer().getLocation());
			
			if(c.exists()){
				if(v.getKingdom().getRelation(c.getKingdom()) == 2){
					event.setCancelled(true);
					event.getPlayer().sendMessage(Messages.getMessage("cmdBlockedInClaim", c.getKingdom().getName()));
					return;
				}
			}
			
			if(f.exists()){
				if(v.getKingdom().getRelation(f.getOwner().getKingdom()) == 2){
					event.setCancelled(true);
					event.getPlayer().sendMessage(Messages.getMessage("cmdBlockedInClaim", f.getOwnerName()));
					return;
				}
			}
		}
		
		if(Governance.mainConfig.getConfig().getList("CommandBlocking.blockedNearEnemyPlayer").contains(cmd[0])){
			Double distance = Governance.mainConfig.getConfig().getDouble("CommandBlocking.blockDistanceNearPlayer");
			for(Entity ent: event.getPlayer().getNearbyEntities(distance, distance, distance)){
				if(ent instanceof Player){
					Vassal vassal = new Vassal(((Player) ent).getName());
					if(v.getKingdom().getRelation(vassal.getKingdom()) == 2){
						event.setCancelled(true);
						event.getPlayer().sendMessage(Messages.getMessage("cmdBlockedNearPlayer", vassal.getName()));
						return;
					}
				}
			}
		}
	}
}
