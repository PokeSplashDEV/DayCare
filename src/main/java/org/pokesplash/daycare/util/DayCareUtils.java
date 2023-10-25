package org.pokesplash.daycare.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.egg.EggGroup;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.pokesplash.daycare.DayCare;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class DayCareUtils {
	public static boolean isCompatible(Pokemon pokemon1, Pokemon pokemon2) throws IllegalPokemonException {

		if (pokemon1 == null || pokemon2 == null) {
			return false;
		}

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
				break;
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

	public static Pokemon makeBaby(Pokemon parent1, Pokemon parent2) {
		Pokemon baby = new Pokemon().initialize();

		// Sets the species.
		baby.setSpecies(getSpecies(parent1, parent2));

		// Sets form
		PokemonProperties properties =
				PokemonProperties.Companion.parse(getForm(parent1, parent2, baby), " ", "=");
		properties.apply(baby);













		return baby;

		// TODO all breeding logic.
	}

	private static Species getSpecies(Pokemon parentOne, Pokemon parentTwo) {
		Species ditto = PokemonSpecies.INSTANCE.getByPokedexNumber(132, Cobblemon.MODID);

		ArrayList<Species> volbeatIllumise = new ArrayList<>();
		Species volbeat = PokemonSpecies.INSTANCE.getByPokedexNumber(313, Cobblemon.MODID);
		Species illumise = PokemonSpecies.INSTANCE.getByPokedexNumber(314, Cobblemon.MODID);
		volbeatIllumise.add(volbeat);
		volbeatIllumise.add(illumise);

		ArrayList<Species> nidoranSpecies = new ArrayList<>();
		Species nidoranMale = PokemonSpecies.INSTANCE.getByPokedexNumber(32, Cobblemon.MODID);
		Species nidoranFemale = PokemonSpecies.INSTANCE.getByPokedexNumber(29, Cobblemon.MODID);
		nidoranSpecies.add(nidoranMale);
		nidoranSpecies.add(nidoranFemale);

		Species parent1 = findLowestEvo(parentOne.getSpecies());
		Species parent2 = findLowestEvo(parentTwo.getSpecies());

		boolean isVolbeatIllumise = parent1.equals(volbeat) || parent1.equals(illumise)
				|| parent2.equals(volbeat) || parent2.equals(illumise);

		boolean isNidoran = parent1.equals(nidoranMale) || parent1.equals(nidoranFemale)
				|| parent2.equals(nidoranMale) || parent2.equals(nidoranFemale);


		if (parent1.equals(ditto)) {
			return isVolbeatIllumise ? Utils.getRandomValue(volbeatIllumise) :
					isNidoran ? Utils.getRandomValue(nidoranSpecies) : parent2;
		} else if (parent2.equals(ditto)) {
			return isVolbeatIllumise ? Utils.getRandomValue(volbeatIllumise) :
					isNidoran ? Utils.getRandomValue(nidoranSpecies) : parent1;
		} else if (parentOne.getGender().equals(Gender.FEMALE)) {
			return parent1.equals(illumise) ? Utils.getRandomValue(volbeatIllumise) :
					parent1.equals(nidoranFemale) ? Utils.getRandomValue(nidoranSpecies) : parent1;
		}  else {
			return parent2.equals(illumise) ? Utils.getRandomValue(volbeatIllumise) :
					parent2.equals(nidoranFemale) ? Utils.getRandomValue(nidoranSpecies) : parent2;
		}
	}

	private static String getForm(Pokemon parent1, Pokemon parent2, Pokemon baby) {

		Item parent1Item = parent1.heldItem().getItem();
		Item parent2Item = parent2.heldItem().getItem();

		Species parent1Species = findLowestEvo(parent1.getSpecies());
		Species parent2Species = findLowestEvo(parent2.getSpecies());


		if (parent1Item.equals(CobblemonItems.EVERSTONE) && parent1Species.equals(baby.getSpecies())) {
			return checkForm(parent1);
		} else if (parent2Item.equals(CobblemonItems.EVERSTONE) && parent2Species.equals(baby.getSpecies())) {
			return checkForm(parent2);
		} else {
			return checkForm(baby);
		}


	}

	private static Species findLowestEvo(Species pokemon) {
		if (pokemon.getPreEvolution() == null) {
			return pokemon;
		} else {
			return findLowestEvo(pokemon.getPreEvolution().getSpecies());
		}
	}

	private static String checkForm(Pokemon pokemon) {
		JsonObject data = pokemon.saveToJSON(new JsonObject());
		boolean isAlolan = data.get("alolan") == null ? false : data.get("alolan").getAsBoolean();
		boolean isGalarian = data.get("galarian") == null ? false : data.get("galarian").getAsBoolean();

		if (isGalarian) {
			return "galarian";
		} else if (isAlolan) {
			return "alolan";
		} else {
			return "normal";
		}
	}
}
