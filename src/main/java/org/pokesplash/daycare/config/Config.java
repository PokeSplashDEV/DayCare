package org.pokesplash.daycare.config;

import com.google.gson.Gson;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Config {
	private boolean allowDittoBreeding;
	private int incubatorAmount;
	private int incubationTime;

	public Config() {
		allowDittoBreeding = true;
		incubatorAmount = 7;
		incubationTime = 60;
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(DayCare.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					allowDittoBreeding = cfg.isAllowDittoBreeding();
					incubatorAmount = cfg.getIncubatorAmount();
					incubationTime = cfg.getIncubationTime();
				});

		if (!futureRead.join()) {
			DayCare.LOGGER.info("No config.json file found for " + DayCare.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(DayCare.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				DayCare.LOGGER.fatal("Could not write config for " + DayCare.MOD_ID + ".");
			}
			return;
		}
		DayCare.LOGGER.info(DayCare.MOD_ID + " config file read successfully");
	}

	public boolean isAllowDittoBreeding() {
		return allowDittoBreeding;
	}

	public int getIncubatorAmount() {
		return incubatorAmount;
	}

	public int getIncubationTime() {
		return incubationTime;
	}
}
