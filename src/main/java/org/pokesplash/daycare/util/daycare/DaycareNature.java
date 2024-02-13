package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;

import java.util.ArrayList;

public class DaycareNature {
	public static Nature getNature(Pokemon parent1, Pokemon parent2) {

		if (parent1.heldItem().getItem().equals(CobblemonItems.EVERSTONE)) {
			return parent1.getNature();
		} else if (parent2.heldItem().getItem().equals(CobblemonItems.EVERSTONE)) {
			return parent2.getNature();
		} else {
			return Natures.INSTANCE.getRandomNature();
		}
	}
}
