package org.pokesplash.daycare.storage;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;
import org.pokesplash.daycare.DayCare;

import java.util.Date;
import java.util.UUID;

public class IncubatorStorage {
	private UUID id;
	private JsonObject pokemonData;
	private long endTime;

	public IncubatorStorage(Pokemon pokemon) {
		id = UUID.randomUUID();
		pokemonData = pokemon.saveToJSON(new JsonObject());
		endTime = new Date().getTime() + ((long) DayCare.config.getIncubationTime() * 60 * 1000);
	}

	public UUID getId() {
		return id;
	}

	public JsonObject getPokemonData() {
		return pokemonData;
	}

	public long getEndTime() {
		return endTime;
	}
}
