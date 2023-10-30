package org.pokesplash.daycare.event.events;

import com.cobblemon.mod.common.pokemon.Pokemon;
import org.pokesplash.daycare.account.Incubator;

import java.util.UUID;

public class RetrieveEggEvent {
	private UUID player;
	private Incubator incubator;
	private Pokemon baby;

	public RetrieveEggEvent(UUID player, Incubator incubator, Pokemon baby) {
		this.player = player;
		this.incubator = incubator;
		this.baby = baby;
	}

	public UUID getPlayer() {
		return player;
	}

	public Incubator getIncubator() {
		return incubator;
	}

	public Pokemon getBaby() {
		return baby;
	}
}
