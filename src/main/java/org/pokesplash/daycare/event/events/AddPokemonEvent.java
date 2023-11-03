package org.pokesplash.daycare.event.events;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.daycare.account.Incubator;

public class AddPokemonEvent {
	private ServerPlayerEntity player;
	private Pokemon pokemon;
	private Incubator incubator;

	public AddPokemonEvent(ServerPlayerEntity player, Pokemon pokemon, Incubator incubator) {
		this.player = player;
		this.pokemon = pokemon;
		this.incubator = incubator;
	}

	public ServerPlayerEntity getPlayer() {
		return player;
	}

	public Pokemon getPokemon() {
		return pokemon;
	}

	public Incubator getIncubator() {
		return incubator;
	}
}
