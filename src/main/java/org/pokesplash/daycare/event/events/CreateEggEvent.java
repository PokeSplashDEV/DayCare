package org.pokesplash.daycare.event.events;

import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.UUID;

public class CreateEggEvent {
	private UUID player;
	private Pokemon parent1;
	private Pokemon parent2;
	private Pokemon baby;

	public CreateEggEvent(UUID player, Pokemon parent1, Pokemon parent2, Pokemon baby) {
		this.player = player;
		this.parent1 = parent1;
		this.parent2 = parent2;
		this.baby = baby;
	}

	public UUID getPlayer() {
		return player;
	}

	public Pokemon getParent1() {
		return parent1;
	}

	public Pokemon getParent2() {
		return parent2;
	}

	public Pokemon getBaby() {
		return baby;
	}
}
