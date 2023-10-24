package org.pokesplash.daycare.storage;

import org.pokesplash.daycare.DayCare;

import java.util.ArrayList;

public class PlayerStorage {
	private ArrayList<IncubatorStorage> incubators;

	public PlayerStorage() {
		incubators = new ArrayList<>();
	}

	public ArrayList<IncubatorStorage> getIncubators() {
		return incubators;
	}

	public void addIncubator(IncubatorStorage incubator) {
		incubators.add(incubator);
		DayCare.storage.write();
	}

	public void remove(IncubatorStorage incubator) {
		incubators.remove(incubator);
		DayCare.storage.write();
	}
}
