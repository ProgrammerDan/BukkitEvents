package com.programmerdan.minecraft.bukkitevents;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import net.minecraft.server.v1_10_R1.IDataManager;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.WorldNBTStorage;
import net.minecraft.server.v1_10_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitEvents extends JavaPlugin implements Listener {
	private long heartbeat;
	private long duration = 50*20l;
	private long tolerance = 50l;
	
	public void beat() {
		long since = System.currentTimeMillis() - heartbeat;
		heartbeat = System.currentTimeMillis();
		if (Math.abs(since - duration) > tolerance) {
			this.getLogger().log(Level.INFO, "{1}] Heartbeat delayed by {0} milliseconds", 
					new Object[] {since, heartbeat});
		} else {
			this.getLogger().log(Level.INFO, "{0}] Heartbeat on time", heartbeat);
		}
		
	}
	
	@Override
	public void onEnable(){
		this.getServer().getPluginManager().registerEvents(this, this);
		setWorldNBTStorage();
		heartbeat = System.currentTimeMillis();
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				beat();
			}
		}, 20l, 20l);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void doAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		forceDelay(event.getEventName()+ "-pre", EventPriority.HIGHEST.name(), 4000l);
		this.getLogger().log(Level.INFO, "{0}] Player UUID Pre-Login: {1}",
				new Object[] {System.currentTimeMillis(), event.getUniqueId()});
		forceDelay(event.getEventName()+ "-post", EventPriority.HIGHEST.name(), 4000l);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void doPlayerJoinEvent(PlayerJoinEvent event) {
		forceDelay(event.getEventName()+ "-pre", EventPriority.HIGHEST.name(), 1000l);
		this.getLogger().log(Level.INFO, "{0}] Player UUID Join: {1}",
				new Object[] {System.currentTimeMillis(), event.getPlayer().getUniqueId()});
		forceDelay(event.getEventName()+ "-post", EventPriority.HIGHEST.name(), 1000l);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void doPlayerLoginEvent(PlayerLoginEvent event) {
		forceDelay(event.getEventName()+ "-pre", EventPriority.HIGHEST.name(), 1000l);
		this.getLogger().log(Level.INFO, "{0}] Player UUID Login: {1}",
				new Object[] {System.currentTimeMillis(), event.getPlayer().getUniqueId()});
		forceDelay(event.getEventName()+ "-post", EventPriority.HIGHEST.name(), 1000l);	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void doPlayerQuitEvent(PlayerQuitEvent event) {
		forceDelay(event.getEventName(), EventPriority.HIGHEST.name(), 2000l);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void doPlayerKickEvent(PlayerKickEvent event) {
		forceDelay(event.getEventName(), EventPriority.HIGHEST.name(), 2000l);
	}
	
	private void forceDelay(String event, String priority, long delay) {
		this.getLogger().log(Level.INFO, "{0}] {1} {2} begin", new Object[] {
				System.currentTimeMillis(), event, priority} );
		
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
			this.getLogger().log(Level.INFO, "{0}] {1} {2} exception", new Object[] {
					System.currentTimeMillis(), event, priority} );
			this.getLogger().log(Level.SEVERE, "Error", e);
		}
		
		this.getLogger().log(Level.INFO, "{0}] {1} {2} end", new Object[] {
				System.currentTimeMillis(), event, priority} );
	}
	
	private void setWorldNBTStorage() {
		for (World w: Bukkit.getWorlds()) {
			WorldServer nmsWorld = ((CraftWorld) w).getHandle();
			Field fieldName;
			try {
				fieldName = net.minecraft.server.v1_10_R1.World.class.getDeclaredField("dataManager");
				fieldName.setAccessible(true);
				
				IDataManager manager = nmsWorld.getDataManager();
				
				// Spigot has a file lock we want to try remove before invoking our own stuff.
				WorldNBTStorage nbtManager = ((WorldNBTStorage) manager);
				
				/*try {
					Field f = nbtManager.getClass().getSuperclass().getDeclaredField("sessionLock");
					f.setAccessible(true);
					FileLock sessionLock = (FileLock) f.get(nbtManager);
					sessionLock.close();
				} catch (Exception e) {
				}*/
				
				CustomWorldNBTStorage newStorage = new CustomWorldNBTStorage(nbtManager, this.getLogger());
				setFinalStatic(fieldName, newStorage, nmsWorld);
				
				MinecraftServer.getServer().getPlayerList().playerFileData = newStorage;
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setFinalStatic(Field field, Object newValue, Object obj) {
		try {
			field.setAccessible(true);

			// remove final modifier from field
			Field modifiersField;
			modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField
					.setInt(field, field.getModifiers() & ~Modifier.PROTECTED);
			
			field.set(obj, newValue);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
