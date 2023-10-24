package org.pokesplash.daycare.account;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;

import java.util.UUID;

public class Incubator {
	private UUID id;
	private JsonObject parent1;
	private JsonObject parent2;
	private JsonObject baby;
	private long endTime;

	public Incubator() {
		id = UUID.randomUUID();
		reset();
	}

	public UUID getId() {
		return id;
	}

	public Pokemon getParent1() {
		if (parent1 == null) {
			return null;
		} else {
			return new Pokemon().loadFromJSON(parent1);
		}
	}

	public void setParent1(Pokemon parent1) {
		this.parent1 = parent1.saveToJSON(new JsonObject());
	}

	public Pokemon getParent2() {
		if (parent2 == null) {
			return null;
		} else {
			return new Pokemon().loadFromJSON(parent2);
		}
	}

	public void setParent2(Pokemon parent2) {
		this.parent2 = parent2.saveToJSON(new JsonObject());
	}

	public Pokemon getBaby() {
		if (baby == null) {
			return null;
		} else {
			return new Pokemon().loadFromJSON(baby);
		}
	}

	public void setBaby(Pokemon baby) {
		this.baby = baby.saveToJSON(new JsonObject());
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void reset() {
		parent1 = null;
		parent2 = null;
		baby = null;
		endTime = -1;
	}
}
