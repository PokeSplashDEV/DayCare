package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;

public abstract class DaycareForm {
	@Deprecated
	public static String getForm(Pokemon parent1, Pokemon parent2, Pokemon baby) {

		Item parent1Item = parent1.heldItem().getItem();
		Item parent2Item = parent2.heldItem().getItem();

		Species parent1Species = DayCareUtils.findLowestEvo(parent1.getForm()).getSpecies();
		Species parent2Species = DayCareUtils.findLowestEvo(parent2.getForm()).getSpecies();


		if (parent1Item.equals(CobblemonItems.EVERSTONE) && parent1Species.equals(baby.getSpecies())) {
			return checkForm(parent1);
		} else if (parent2Item.equals(CobblemonItems.EVERSTONE) && parent2Species.equals(baby.getSpecies())) {
			return checkForm(parent2);
		} else {
			return checkForm(baby);
		}
	}

	@Deprecated
	private static String checkForm(Pokemon pokemon) {
		JsonObject data = pokemon.saveToJSON(new JsonObject());
		boolean isAlolan = data.get("alolan") == null ? false : data.get("alolan").getAsBoolean();
		boolean isGalarian = data.get("galarian") == null ? false : data.get("galarian").getAsBoolean();
		boolean isPaldean = data.get("paldean") == null ? false : data.get("paldean").getAsBoolean();

		if (isGalarian) {
			return "galarian";
		} else if (isAlolan) {
			return "alolan";
		} else if (isPaldean) {
			return "paldean";
		} else {
			return "normal";
		}
	}
}
