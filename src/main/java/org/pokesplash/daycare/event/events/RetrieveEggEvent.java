package org.pokesplash.daycare.event.events;

import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.daycare.account.Incubator;

public class RetrieveEggEvent {
	private ServerPlayerEntity player;
	private Incubator incubator;
	private Pokemon baby;

	public RetrieveEggEvent(ServerPlayerEntity player, Incubator incubator, Pokemon baby) {
		this.player = player;
		this.incubator = incubator;
		this.baby = baby;
	}

	public ServerPlayerEntity getPlayer() {
		return player;
	}

	public Incubator getIncubator() {
		return incubator;
	}

	public Pokemon getBaby() {
		return baby;
	}
}
