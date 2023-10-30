package org.pokesplash.daycare.event.events;

import com.cobblemon.mod.common.pokemon.Pokemon;
import org.pokesplash.daycare.account.Incubator;

import java.util.UUID;

public class RemovePokemonEvent {
	private UUID player;
	private Pokemon pokemon;
	private Incubator incubator;

	public RemovePokemonEvent(UUID player, Pokemon pokemon, Incubator incubator) {
		this.player = player;
		this.pokemon = pokemon;
		this.incubator = incubator;
	}

	public UUID getPlayer() {
		return player;
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public Incubator getIncubator() {
		return incubator;
	}
}
