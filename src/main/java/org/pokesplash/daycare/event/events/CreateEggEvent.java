package org.pokesplash.daycare.event.events;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;

public class CreateEggEvent {
	private ServerPlayerEntity player;
	private Pokemon parent1;
	private Pokemon parent2;
	private Pokemon baby;

	public CreateEggEvent(ServerPlayerEntity player, Pokemon parent1, Pokemon parent2, Pokemon baby) {
		this.player = player;
		this.parent1 = parent1;
		this.parent2 = parent2;
		this.baby = baby;
	}

	public ServerPlayerEntity getPlayer() {
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
