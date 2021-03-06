package org.theglicks.bukkit.governance;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.theglicks.bukkit.governance.landManagement.Claim;
import org.theglicks.bukkit.governance.landManagement.Fief;
import org.theglicks.bukkit.governance.landManagement.KingdomLandClaim;

public class Vassal{
	private String name;
	private DataStore vData = new DataStore();
	
	public Vassal(String vassalName){
		try {
			name = vassalName;
			vData.rs = vData.st.executeQuery("SELECT * FROM `governance`.`vassals` WHERE `name` = '" + name + "';");
			vData.rs.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Kingdom getKingdom(){
		try {
			DataStore ds = new DataStore();
			ds.rs = ds.st.executeQuery("SELECT `name` FROM `governance`.`kingdoms` WHERE `id` = " + vData.rs.getInt("kingdom") + ";");
			ds.rs.first();
			
			return new Kingdom(ds.rs.getString("name"));
		} catch (SQLException e) {
			e.printStackTrace();
		} return null;
	}
	
	public Player getPlayer(){
		return Bukkit.getPlayer(name);
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isAllied(Vassal v){
		return getKingdom().isAllied(v.getKingdom());
	}
	
	public boolean canBuild(Claim claim){
		if(claim instanceof Fief){
			Fief f = (Fief) claim;
			if(!(f.getOwnerName().equals(name))) return false;
		} else if (claim instanceof KingdomLandClaim){
			KingdomLandClaim c = (KingdomLandClaim) claim;
			
			if(!(hasKingdom())) return false;
			
			if(!(c.getKingdom().getName().equals(getKingdom().getName()))) return false;
		}
		return true;
	}
	
	public boolean canBuild(Location loc){
		KingdomLandClaim c = new KingdomLandClaim(loc);
		Fief f = new Fief(loc);
		
		if(c.exists()){
			return canBuild(c);
		}
		
		if(f.exists()){
			return canBuild(f);
		}
		return true;
	}
	
	public boolean canCreateKingdom(){
		if(getPlayer().hasPermission("governance.basic.createKingdom")){
			return true;
		} return false;
	}
	
	public boolean hasKingdom(){
		try {
			vData.rs.getInt("kingdom");
			if(vData.rs.wasNull()) return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean isLeader(){
		try {
			return vData.rs.getBoolean("leader");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int getId(){
		int id = 0;
		try {
			id = vData.rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public boolean hasInvite(Kingdom k){
		try {
			DataStore ds = new DataStore();
			ds.rs = ds.st.executeQuery("SELECT * FROM `governance`.`invitations` WHERE `kingdom` = " + k.getId() + " AND `vassal` = "
					+ getId());
			if(ds.rs.next()) return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void setKingdom(Kingdom k){
		try {
			DataStore ds = new DataStore();
			ds.st.execute("UPDATE `governance`.`vassals` SET `kingdom`='" + k.getId() + "' WHERE `id`='" + getId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeKingdom(){
		try {
			DataStore ds = new DataStore();
			ds.st.execute("UPDATE `governance`.`vassals` SET `kingdom`= null WHERE `id`='" + getId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setLeader(boolean arg){
		try {
			DataStore ds = new DataStore();
			int bool;
			if (arg)
				bool = 1;
			else
				bool = 0;
			ds.st.execute("UPDATE `governance`.`vassals` SET `leader`=" + bool + " WHERE `id`='" + getId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}