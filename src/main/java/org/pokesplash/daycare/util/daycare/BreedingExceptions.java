package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import org.pokesplash.daycare.util.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class BreedingExceptions {
	private static final Species ditto = PokemonSpecies.INSTANCE.getByPokedexNumber(132, Cobblemon.MODID);
	private static final Species nidoranMale = PokemonSpecies.INSTANCE.getByPokedexNumber(32, Cobblemon.MODID);
	private static final Species nidoranFemale = PokemonSpecies.INSTANCE.getByPokedexNumber(29, Cobblemon.MODID);
	private static final Species volbeat = PokemonSpecies.INSTANCE.getByPokedexNumber(313, Cobblemon.MODID);
	private static final Species illumise = PokemonSpecies.INSTANCE.getByPokedexNumber(314, Cobblemon.MODID);



	public static boolean isNidoran(Pokemon pokemon) {
		ArrayList<Species> nidoranSpecies = getNidoranList();
		return nidoranSpecies.contains(DayCareUtils.findLowestEvo(pokemon.getForm()).getSpecies());
	}

	public static Species getNidoran() {
		ArrayList<Species> nidoranSpecies = getNidoranList();
		return Utils.getRandomValue(nidoranSpecies);
	}

	public static Pokemon getNidoranParent(Pokemon parent1, Pokemon parent2) {
		if (isNidoran(parent1)) {
			return parent1;
		} else {
			return parent2;
		}
	}

	public static boolean isVolbeatIllumise(Pokemon pokemon) {
		ArrayList<Species> volbeatIllumise = getVolbeatIllumiseList();
		return volbeatIllumise.contains(DayCareUtils.findLowestEvo(pokemon.getForm()).getSpecies());
	}

	public static Species getVolbeatIllumise() {
		ArrayList<Species> volbeatIllumise = getVolbeatIllumiseList();
		return Utils.getRandomValue(volbeatIllumise);
	}

	public static Pokemon getVolbeatIllumiseParent(Pokemon parent1, Pokemon parent2) {
		if (isVolbeatIllumise(parent1)) {
			return parent1;
		} else {
			return parent2;
		}
	}

	private static ArrayList<Species> getNidoranList() {
		ArrayList<Species> nidoranSpecies = new ArrayList<>();
		nidoranSpecies.add(nidoranMale);
		nidoranSpecies.add(nidoranFemale);
		return nidoranSpecies;
	}

	private static ArrayList<Species> getVolbeatIllumiseList() {
		ArrayList<Species> volbeatIllumise = new ArrayList<>();
		volbeatIllumise.add(volbeat);
		volbeatIllumise.add(illumise);
		return volbeatIllumise;
	}

	public static boolean hasDitto(Pokemon parent1, Pokemon parent2) {
		return parent1.getSpecies().equals(ditto) ||
				parent2.getSpecies().equals(ditto);
	}

	public static Pokemon getNonDitto(Pokemon parent1, Pokemon parent2) {
		if (parent1.getSpecies().equals(ditto)) {
			return parent1;
		} else {
			return parent2;
		}
	}

	public static boolean matchesPokemon(Pokemon pokemon1, Pokemon pokemon2) {
		if (isNidoran(pokemon1) && isNidoran(pokemon2)) {
			return true;
		}

		if (isVolbeatIllumise(pokemon1) && isVolbeatIllumise(pokemon2)) {
			return true;
		}

		return DayCareUtils.findLowestEvo(pokemon1.getForm()).equals(
				DayCareUtils.findLowestEvo(pokemon2.getForm()));
	}
}
