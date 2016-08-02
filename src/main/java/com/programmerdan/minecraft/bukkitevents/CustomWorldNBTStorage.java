package com.programmerdan.minecraft.bukkitevents;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.v1_10_R1.DefinedStructureManager;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.ExceptionWorldConflict;
import net.minecraft.server.v1_10_R1.IChunkLoader;
import net.minecraft.server.v1_10_R1.IPlayerFileData;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.WorldData;
import net.minecraft.server.v1_10_R1.WorldNBTStorage;
import net.minecraft.server.v1_10_R1.WorldProvider;

public class CustomWorldNBTStorage implements net.minecraft.server.v1_10_R1.IDataManager, net.minecraft.server.v1_10_R1.IPlayerFileData {
	private WorldNBTStorage wrapped;
	private Logger log;
	
	public CustomWorldNBTStorage(WorldNBTStorage toWrap, Logger log) {
		this.wrapped = toWrap;
		this.log = log;
	}
	
	private void entry() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		String mname = "Unknown";
		if (ste != null && ste.length > 2) {
			mname = ste[2].getMethodName();
		} 
		log.log(Level.INFO, "{1}] Entered {0}", new Object[] {mname, System.currentTimeMillis()});
	}
	
	private void exit() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		String mname = "Unknown";
		if (ste != null && ste.length > 2) {
			mname = ste[2].getMethodName();
		} 
		log.log(Level.INFO, "{1}] Exiting {0}", new Object[] {mname, System.currentTimeMillis()});
	}

	@Override
	public String[] getSeenPlayers() {
		try {
			entry();
			return wrapped.getSeenPlayers();
		} finally {
			exit();
		}
	}

	@Override
	public NBTTagCompound load(EntityHuman arg0) {
		try {
			entry();
			return wrapped.load(arg0);
		} finally {
			exit();
		}
	}

	@Override
	public void save(EntityHuman arg0) {
		try {
			entry();
			wrapped.save(arg0);
		} finally {
			exit();
		}
	}

	@Override
	public void a() {
		try {
			entry();
			wrapped.a();
		} finally {
			exit();
		}
	}

	@Override
	public void checkSession() throws ExceptionWorldConflict {
		try {
			entry();
			wrapped.checkSession();
		} finally {
			exit();
		}
	}

	@Override
	public IChunkLoader createChunkLoader(WorldProvider arg0) {
		try {
			entry();
			return wrapped.createChunkLoader(arg0);
		} finally {
			exit();
		}
	}

	@Override
	public File getDataFile(String arg0) {
		try {
			entry();
			return wrapped.getDataFile(arg0);
		} finally {
			exit();
		}
	}

	@Override
	public File getDirectory() {
		try {
			entry();
			return wrapped.getDirectory();
		} finally {
			exit();
		}
	}

	@Override
	public IPlayerFileData getPlayerFileData() {
		try {
			entry();
			return wrapped.getPlayerFileData();
		} finally {
			exit();
		}
	}

	@Override
	public UUID getUUID() {
		try {
			entry();
			return wrapped.getUUID();
		} finally {
			exit();
		}
	}

	@Override
	public WorldData getWorldData() {
		try {
			entry();
			return wrapped.getWorldData();
		} finally {
			exit();
		}
	}

	@Override
	public DefinedStructureManager h() {
		try {
			entry();
			return wrapped.h();
		} finally {
			exit();
		}
	}

	@Override
	public void saveWorldData(WorldData arg0) {
		try {
			entry();
			wrapped.saveWorldData(arg0);
		} finally {
			exit();
		}
	}

	@Override
	public void saveWorldData(WorldData arg0, NBTTagCompound arg1) {
		try {
			entry();
			wrapped.saveWorldData(arg0, arg1);
		} finally {
			exit();
		}
	}
}
