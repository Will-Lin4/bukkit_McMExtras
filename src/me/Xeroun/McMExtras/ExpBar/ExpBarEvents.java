package me.Xeroun.McMExtras.ExpBar;

import me.Xeroun.McMExtras.McMExtras;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;

public class ExpBarEvents implements Listener{

	@EventHandler
	public void onExpGain(McMMOPlayerXpGainEvent event) {
		McMExtras.getInstance().getData(event.getPlayer().getName()).setSkill(event.getSkill().name());
		McMExtras.getInstance().getData(event.getPlayer().getName()).updateExpBar();
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		McMExtras.getInstance().clearData(event.getPlayer().getName());
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		McMExtras.getInstance().getData(event.getPlayer().getName()).updateExpBar();
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		McMExtras.getInstance().getData(event.getPlayer().getName()).updateExpBar();
	}

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		McMExtras.getInstance().getData(event.getPlayer().getName()).updateExpBar();
	}
	
}
