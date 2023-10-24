package org.pokesplash.daycare.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.data.JsonDataRegistry;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.egg.EggGroup;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import org.pokesplash.daycare.DayCare;

public abstract class DayCareUtils {
	public static boolean isCompatible(Pokemon pokemon1, Pokemon pokemon2) throws IllegalPokemonException {

		Species ditto = PokemonSpecies.INSTANCE.getByPokedexNumber(132, Cobblemon.MODID);

		// If both are ditto, donut breed.
		if (pokemon1.getSpecies().equals(ditto) && pokemon2.getSpecies().equals(ditto)) {
			throw new IllegalPokemonException("Ditto can not breed with Ditto.");
		}

		// If either Pokemon are undiscovered, throw error.
		if (isUndiscovered(pokemon1) || isUndiscovered(pokemon2)) {
			throw new IllegalPokemonException("At least one Pokemon is in the Undiscovered egg group.");
		}

		// If one is ditto, breed.
		if (pokemon1.getSpecies().equals(ditto) || pokemon2.getSpecies().equals(ditto)) {
			if (DayCare.config.isAllowDittoBreeding()) {
				return true;
			} else {
				throw new IllegalPokemonException("Ditto breeding is not allowed on the server.");
			}
		}

		// Checks Egg Groups.
		boolean matchesEgggroup = false;
		for (EggGroup eggGroup : pokemon1.getSpecies().getEggGroups()) {
			if (pokemon2.getSpecies().getEggGroups().contains(eggGroup)) {
				matchesEgggroup = true;
			}
		}
		if (!matchesEgggroup) {
			throw new IllegalPokemonException("Pokemon do not have matching egg groups.");
		}

		// Check genders are opposite and not genderless.
		Gender mon1Gender = pokemon1.getGender();
		Gender mon2Gender = pokemon2.getGender();

		// If either gender are genderless, or genders match, throw exception.
		if (mon1Gender.equals(Gender.GENDERLESS) || mon2Gender.equals(Gender.GENDERLESS) ||
			mon1Gender.equals(mon2Gender)) {
			throw new IllegalPokemonException("Pokemon do not have different genders, or one Pokemon is genderless");
		}

		return true;
	}

	private static boolean isUndiscovered(Pokemon pokemon) {
		return pokemon.getSpecies().getEggGroups().contains(EggGroup.UNDISCOVERED);
	}

	public static String debugCompatible(Pokemon pokemon1, Pokemon pokemon2) {
		try {
			DayCareUtils.isCompatible(pokemon1, pokemon2);
			return "Compatible Pokemon";
		} catch (IllegalPokemonException e) {
			return e.getMessage();
		}
	}
}
