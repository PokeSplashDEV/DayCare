package org.pokesplash.daycare.config;

import com.google.gson.Gson;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;

	public Lang() {
		title = DayCare.MOD_ID;
		fillerMaterial = "minecraft:white_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(DayCare.BASE_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
				});

		if (!futureRead.join()) {
			DayCare.LOGGER.info("No lang.json file found for " + DayCare.MOD_ID + ". Attempting to " +
					"generate " +
					"one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(DayCare.BASE_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				DayCare.LOGGER.fatal("Could not write lang.json for " + DayCare.MOD_ID + ".");
			}
			return;
		}
		DayCare.LOGGER.info(DayCare.MOD_ID + " lang file read successfully.");
	}
}
