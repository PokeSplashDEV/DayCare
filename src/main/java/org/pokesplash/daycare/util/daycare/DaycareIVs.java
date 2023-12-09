package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokemon.Pokemon;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DaycareIVs {
	public static HashMap<Stat, Integer> getIVs(Pokemon parent1, Pokemon parent2) {
		ArrayList<Stat> baseStats = new ArrayList<>();
		baseStats.add(Stats.HP);
		baseStats.add(Stats.ATTACK);
		baseStats.add(Stats.DEFENCE);
		baseStats.add(Stats.SPECIAL_ATTACK);
		baseStats.add(Stats.SPECIAL_DEFENCE);
		baseStats.add(Stats.SPEED);

		// Looks for power item on parents.
		Stat stat1 = getItemStat(parent1);
		Stat stat2 = getItemStat(parent2);

		HashMap<Stat, Integer> newStats = new HashMap<>();

		// If a stat was returned, add the pokemons stat to the new stats.
		if (stat1 != null) {
			newStats.put(stat1, parent1.getIvs().get(stat1));
			baseStats.remove(stat1);
		}

		if (stat2 != null) {
			newStats.put(stat2, parent2.getIvs().get(stat2));
			baseStats.remove(stat2);
		}

		// If either parent have a destiny knot, add 5 ivs instead of 3.
		if (parent1.heldItem().getItem().equals(CobblemonItems.DESTINY_KNOT) ||
				parent2.heldItem().getItem().equals(CobblemonItems.DESTINY_KNOT)) {
			for (int x=newStats.size(); x<5; x++) {
				Stat stat = Utils.getRandomValue(baseStats);
				Pokemon parent = Utils.getRandomValue(Arrays.asList(parent1, parent2));
				newStats.put(stat, parent.getIvs().get(stat) == null ? 1 : Integer.valueOf(parent.getIvs().get(stat)));
				baseStats.remove(stat);
			}
		} else {
			for (int x=newStats.size(); x<3; x++) {
				Stat stat = Utils.getRandomValue(baseStats);
				Pokemon parent = Utils.getRandomValue(Arrays.asList(parent1, parent2));
				newStats.put(stat, parent.getIvs().get(stat) == null ? 1 : Integer.valueOf(parent.getIvs().get(stat)));
				baseStats.remove(stat);
			}
		}

		for (Stat stat : newStats.keySet()) {
			if (newStats.get(stat) == null) {
				newStats.put(stat, 1);
			}
		}

		return newStats;
	}

	private static Stat getItemStat(Pokemon pokemon) {
		if (pokemon.heldItem().getItem().equals(CobblemonItems.POWER_WEIGHT)) {
			return Stats.HP;
		}

		if (pokemon.heldItem().getItem().equals(CobblemonItems.POWER_BRACER)) {
			return Stats.ATTACK;
		}

		if (pokemon.heldItem().getItem().equals(CobblemonItems.POWER_BELT)) {
			return Stats.DEFENCE;
		}

		if (pokemon.heldItem().getItem().equals(CobblemonItems.POWER_LENS)) {
			return Stats.SPECIAL_ATTACK;
		}

		if (pokemon.heldItem().getItem().equals(CobblemonItems.POWER_BAND)) {
			return Stats.SPECIAL_DEFENCE;
		}

		if (pokemon.heldItem().getItem().equals(CobblemonItems.POWER_ANKLET)) {
			return Stats.SPEED;
		}

		return null;
	}
}
