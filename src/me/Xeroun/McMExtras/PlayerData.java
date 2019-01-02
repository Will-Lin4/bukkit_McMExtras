package me.Xeroun.McMExtras;

import me.Xeroun.McMExtras.ExpBar.FakeDragon;

import org.bukkit.Bukkit;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.skills.SkillType;

public class PlayerData {

	private String player;
	private String lastSkill;
	private int time;
	private boolean enabled;
	private int repeater;
	
	public PlayerData(final String player){
		this.player = player;
		time = 15;
		enabled = true;
		
		repeater = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(McMExtras.getInstance(), new Runnable() {
			public void run() {
				if(Bukkit.getPlayer(player) == null){
					Bukkit.getServer().getScheduler().cancelTask(repeater);
					return;
				}
				if(time >= 0){
					time--;
					if(time <= 0){
						FakeDragon.setBossBar(Bukkit.getPlayer(player), "", 100);
					}
				}
			}
		}, 0, 20L);
		
	}
	
	public String getPlayer(){
		return player;
	}
	
	public String getLastSkill(){
		return lastSkill;
	}

	public boolean enabled(){
		return enabled;
	}
	
	public void setSkill(String skill){
		lastSkill = skill;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public void updateExpBar(){
		if(!enabled) return;
		if(lastSkill == null) return;
		if(player == null) return;
		if(SkillType.getSkill(lastSkill) == null) return;
		if(Bukkit.getPlayer(player) == null) return;
		
		String color;
		
		if(McMExtras.getInstance().getConfig().getString("Experience Bar.Color." + lastSkill).replaceAll("&", "") != null){
			color = McMExtras.getInstance().getConfig().getString("Experience Bar.Color." + lastSkill).replaceAll("&", "");
		}else{
			color = "6";
		}
		
		int exp = ExperienceAPI.getXP(Bukkit.getPlayer(player), lastSkill);
		int requiredExp = ExperienceAPI.getXPToNextLevel(Bukkit.getPlayer(player), lastSkill);
		int percent = (int) (((double) exp / (double) requiredExp) * 100);
		if(percent == 0) percent = 1;
		if(percent >= 100) percent = 99;
		String format = McMExtras.getInstance().getConfig().getString("Experience Bar.Format").replaceAll("@skill", lastSkill.substring(0, 1) + lastSkill.substring(1).toLowerCase()).replaceAll("@level", ExperienceAPI.getLevel(Bukkit.getPlayer(player), lastSkill) + "").replaceAll("@exp", exp + "").replaceAll("@reqExp", requiredExp + "").replaceAll("@percent", percent + "");
		try {
			FakeDragon.setBossBar(Bukkit.getPlayer(player), "§" + color + format, percent);
			time = McMExtras.getInstance().getConfig().getInt("Experience Bar.Seconds until disappear");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
