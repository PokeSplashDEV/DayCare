package org.pokesplash.daycare.storage;

import com.google.gson.Gson;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.util.Utils;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Storage {
	HashMap<UUID, PlayerStorage> storage;

	public Storage() {
		storage = new HashMap<>();
	}

	public PlayerStorage getStorage(UUID player) {
		return storage.get(player);
	}

	public HashMap<UUID, PlayerStorage> getStorage() {
		return storage;
	}

	public void write() {
		Gson gson = Utils.newGson();
		Utils.writeFileAsync(DayCare.BASE_PATH,
				"storage.json", gson.toJson(this));
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(DayCare.BASE_PATH,
				"storage.json", el -> {
					Gson gson = Utils.newGson();
					Storage cfg = gson.fromJson(el, Storage.class);
					storage = cfg.getStorage();
				});

		if (!futureRead.join()) {
			DayCare.LOGGER.info("No storage.json file found for " + DayCare.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(DayCare.BASE_PATH,
					"storage.json", data);

			if (!futureWrite.join()) {
				DayCare.LOGGER.fatal("Could not write storage for " + DayCare.MOD_ID + ".");
			}
			return;
		}
		DayCare.LOGGER.info(DayCare.MOD_ID + " storage file read successfully");
	}
}
