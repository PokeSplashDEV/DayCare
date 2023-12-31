package org.pokesplash.daycare.account;

import com.google.gson.Gson;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Account {
	private UUID owner;
	private HashMap<UUID, Incubator> incubators;

	public Account(UUID owner) {
		this.owner = owner;
		incubators = new HashMap<>();
		updateIncubators();
		write();
	}

	public UUID getOwner() {
		return owner;
	}

	public HashMap<UUID, Incubator> getIncubators() {
		return incubators;
	}

	public Incubator getIncubator(UUID id) {
		return incubators.get(id);
	}

	public void updateIncubator(Incubator incubator) {
		incubators.put(incubator.getId(), incubator);
		write();
	}

	public void write() {
		Gson gson = Utils.newGson();
		CompletableFuture<Boolean> future = Utils.writeFileAsync(DayCare.BASE_PATH + "accounts/", owner.toString() +
						".json",
				gson.toJson(this));

		if (!future.join()) {
			DayCare.LOGGER.error("Could not write account for " + owner.toString() + " in DayCare.");
		}
	}

	public void updateIncubators() {
		int amount = DayCare.config.getIncubatorAmount();
		if (incubators.size() < amount) {
			int difference = amount - incubators.size();
			for (int x=0; x < difference; x++) {
				Incubator newIncubator = new Incubator(owner);
				incubators.put(newIncubator.getId(), newIncubator);
			}
			write();
		}

		if (incubators.size() > amount) {
			ArrayList<UUID> keys = new ArrayList<>(incubators.keySet());
			int difference = incubators.size() - amount;

			for (UUID id : keys) {
				Incubator incubator = incubators.get(id);
				if (difference > 0) {
					if (incubator.getParent2() == null && incubator.getParent1() == null) {
						incubators.remove(id);
						difference --;
					}
				}
			}
			write();
		}

	}
}
