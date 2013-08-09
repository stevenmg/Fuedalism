package org.theglicks.bukkit.fuedalism.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.theglicks.bukkit.fuedalism.Kingdom;
import org.theglicks.bukkit.fuedalism.Vassal;

public class KingdomEnemy {
	public static void execute(CommandSender sender, String[] args){
		//Make sure command is being executed by a player
		if (!(sender instanceof Player)) return;
		
		Vassal v = new Vassal(sender.getName());
		
		if(args.length == 2){
			Kingdom k1 = v.getKingdom();
			Kingdom k2 = new Kingdom(args[1]);
			k1.setRelation(k2, 2);
		}
	}
}