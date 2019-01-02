package me.Xeroun.McMExtras.ExpBar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FakeDragon {

	/*
	 * Class made by BigTeddy98.
	 * Class modified by Xeroun.
	 * 
	 * BossBarFactory is a class to set a boss bar with your custom text to a
	 * player
	 * 
	 * 1. No warranty is given or implied. 2. All damage is your own
	 * responsibility. 3. If you want to use this in your plugins, a credit
	 * would we appreciated.
	 */

	// everything needed for our reflection
	private static Constructor<?> packetPlayOutSpawnEntityLiving;
	private static Constructor<?> packetPlayOutEntityDestroy;
	private static Constructor<?> entityEnderdragon;
	private static Method setLocation;
	
	private static Method getWorldHandle;
	private static Method getPlayerHandle;
	private static Field playerConnection;
	private static Method sendPacket;

	private static Method getDatawatcher;
	private static Method a;
	private static Field d;
	
	private static int ID;
	
	static{
		try {
			packetPlayOutSpawnEntityLiving = getMCClass("PacketPlayOutSpawnEntityLiving").getConstructor(getMCClass("EntityLiving"));
			packetPlayOutEntityDestroy = getMCClass("PacketPlayOutEntityDestroy").getConstructor(int[].class);
			
			entityEnderdragon = getMCClass("EntityEnderDragon").getConstructor(getMCClass("World"));
			setLocation = getMCClass("EntityEnderDragon").getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
			
			getWorldHandle = getCraftClass("CraftWorld").getMethod("getHandle");
			getPlayerHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
			playerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
			sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));
			
			getDatawatcher = getMCClass("EntityEnderDragon").getMethod("getDataWatcher");
			a = getMCClass("DataWatcher").getMethod("a", int.class, Object.class);
			d = getMCClass("DataWatcher").getDeclaredField("d");
			d.setAccessible(true);
			
			ID = 12345;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Map<String, Object> playerDragons = new HashMap<String, Object>();
	private static Map<String, String> playerText = new HashMap<String, String>();
	
	private static Object getEnderDragon(Player p) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		if (playerDragons.containsKey(p.getName())) {
			return playerDragons.get(p.getName());
		} else {
			Object nms_world = getWorldHandle.invoke(p.getWorld());
			playerDragons.put(p.getName(), entityEnderdragon.newInstance(nms_world));
			return getEnderDragon(p);
		}
	}
	
	private static void sendSpawnPacket(Player p, String text, int percent){
		try{
			Object nms_dragon = getEnderDragon(p);
			setLocation.invoke(nms_dragon, p.getLocation().getX(), -300, p.getLocation().getZ(), 0F, 0F);
			changeWatcher(nms_dragon, text);
			
			double percentt = percent;
			if(percentt > 100){
				percentt = 100;
			}else if(percentt < 1){
				percentt = 1;
			}
			percentt = percentt * 0.01;
			changeWatcher(nms_dragon, (float) (200F * percentt));
			Object nms_packet = packetPlayOutSpawnEntityLiving.newInstance(nms_dragon);
			Object nms_player = getPlayerHandle.invoke(p);
			Object nms_connection = playerConnection.get(nms_player);
			Field a = getMCClass("PacketPlayOutSpawnEntityLiving").getDeclaredField("a");
			a.setAccessible(true);
			a.set(nms_packet, ID);
			sendPacket.invoke(nms_connection, nms_packet);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private static void sendDestroyPacket(Player p){
		try{
			Object nms_packet = packetPlayOutEntityDestroy.newInstance(new int[]{ID});
			Object nms_player = getPlayerHandle.invoke(p);
			Object nms_connection = playerConnection.get(nms_player);
			sendPacket.invoke(nms_connection, nms_packet);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public static void setBossBar(Player p, String text, int percent) {
		try {
			if(text.equalsIgnoreCase("")){
				sendDestroyPacket(p);
				return;
			}
			if(playerText.get(p) != null){
				if(!playerText.get(p).equalsIgnoreCase("")){
					sendDestroyPacket(p);
				}
			}
			
			if(!text.equalsIgnoreCase("")){
				sendSpawnPacket(p, text, percent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void changeWatcher(Object nms_entity, String text) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object nms_watcher = getDatawatcher.invoke(nms_entity);
		Map<?, ?> map = (Map<?, ?>) d.get(nms_watcher);
		map.remove(10);
		a.invoke(nms_watcher, 10, text);
	}

	private static void changeWatcher(Object nms_entity, float health) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object nms_watcher = getDatawatcher.invoke(nms_entity);
		Map<?, ?> map = (Map<?, ?>) d.get(nms_watcher);
		map.remove(6);
		a.invoke(nms_watcher, 6, health);
	}
	
	private static Class<?> getMCClass(String name) throws ClassNotFoundException {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String className = "net.minecraft.server." + version + name;
		return Class.forName(className);
	}

	private static Class<?> getCraftClass(String name) throws ClassNotFoundException {
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String className = "org.bukkit.craftbukkit." + version + name;
		return Class.forName(className);
	}
}