package org.theglicks.bukkit.governance.landManagement;

import java.sql.SQLException;

import org.bukkit.Location;
import org.theglicks.bukkit.governance.DataStore;
import org.theglicks.bukkit.governance.Kingdom;

public class KingdomLandClaim extends Claim {
	DataStore cData = new DataStore();
	DataStore kData = new DataStore();
	Kingdom kingdom;
	
	public KingdomLandClaim(Location loc){
		try {
			cData.rs = cData.st.executeQuery("SELECT * FROM `governance`.`kingdomclaims` WHERE MBRCONTAINS("
					+ "`region`, POINT(" + loc.getX() + "," + loc.getZ() + "))"
					+ " AND `world` = '" + loc.getWorld().getName() + "';");
			cData.rs.first();
			
			if (exists()) {
				int kId = cData.rs.getInt("kingdom");
				kData.rs = kData.st.executeQuery("SELECT `name` FROM `governance`.`kingdoms` WHERE `id` = " + kId + ";");
				kData.rs.first();
				kingdom = new Kingdom(kData.rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public KingdomLandClaim(LandSelection select){
		try {
			cData.rs = cData.st.executeQuery("SELECT * FROM `governance`.`kingdomclaims` WHERE MBRIntersects(" + select.getPolygonText()
					+ ", `region`)");
			cData.rs.first();
			
			if (exists()) {
				int kId = cData.rs.getInt("kingdom");
				kData.rs = kData.st.executeQuery("SELECT `name` FROM `governance`.`kingdoms` WHERE `id` = " + kId + ";");
				kData.rs.first();
				kingdom = new Kingdom(kData.rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static KingdomLandClaim create(Location corner, Location corner0, Kingdom k){
		try {
			DataStore cOwner = new DataStore();
			DataStore cData = new DataStore();
			cOwner.rs = cOwner.st.executeQuery("SELECT `id` FROM `governance`.`kingdoms` WHERE `name` = '" + k.getName() + "';");
			cOwner.rs.first();
			String kId = cOwner.rs.getString("id");
			cData.st.execute("INSERT INTO `governance`.`kingdomclaims` (" +
					"`region`, `kingdom`, `world`) VALUES (PolygonFromText('POLYGON((" + 
					corner.getX() + " " + corner.getZ() + ", " +
					corner0.getX() + " " + corner0.getZ() + ", " +
					corner.getX() + " " + corner0.getZ() + ", " +
					corner0.getX() + " " + corner.getZ() + ", " +
					corner.getX() + " " + corner.getZ() + "))'), '" +
					kId + "', '" + corner.getWorld().getName() + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new KingdomLandClaim(corner);
	}
	
	public boolean exists(){
		try {
			if(cData.rs.first()){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} return false;
	}
	
	public void delete(){
		try {
			int id = cData.rs.getInt("id");
			cData.st.execute("DELETE FROM `governance`.`kingdomClaims` WHERE `id`='" + id + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Kingdom getKingdom(){
		return kingdom;
	}
}
