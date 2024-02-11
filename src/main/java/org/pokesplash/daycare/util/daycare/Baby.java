package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;

public abstract class Baby {
	public static Pokemon getBaby(Pokemon parentOne, Pokemon parentTwo) {

		boolean isVolbeatIllumise =
				BreedingExceptions.isVolbeatIllumise(parentOne) ||
						BreedingExceptions.isVolbeatIllumise(parentTwo);

		boolean isNidoran = BreedingExceptions.isNidoran(parentOne) ||
				BreedingExceptions.isNidoran(parentTwo);


		if(BreedingExceptions.hasDitto(parentOne, parentTwo))
		{
			if(isVolbeatIllumise)
			{
				return BreedingExceptions.getVolbeatIllumise().create(1);
			}
			if(isNidoran)
			{
				return BreedingExceptions.getNidoran().create(1);
			}
			// Gets the non-ditto parent and returns their species.
			Pokemon nonDitto = BreedingExceptions.getNonDitto(parentOne, parentTwo);
			return findBaby(
					nonDitto,
					nonDitto.equals(parentOne) ? parentTwo : parentOne);
		}

		if (BreedingExceptions.isVolbeatIllumise(DayCareUtils.getMother(parentOne, parentTwo))) {
			return BreedingExceptions.getVolbeatIllumise().create(1);
		}

		if (BreedingExceptions.isNidoran(DayCareUtils.getMother(parentOne, parentTwo))) {
			return BreedingExceptions.getNidoran().create(1);
		}

		Pokemon mother = DayCareUtils.getMother(parentOne, parentTwo);
		return findBaby(
				mother, mother.equals(parentOne) ? parentTwo : parentOne);
	}

	private static Pokemon findBaby(Pokemon mother, Pokemon other) {
		if (mother.getPreEvolution() == null) {
			if (mother.heldItem().getItem().equals(CobblemonItems.EVERSTONE)) {
				return mother.clone(true, true);
			} else if (other.heldItem().getItem().equals(CobblemonItems.EVERSTONE) &&
					DayCareUtils.findLowestEvo(mother.getForm()).getSpecies().equals(
							DayCareUtils.findLowestEvo(other.getForm()).getSpecies()
					)) {
				Pokemon newBaby = new Pokemon();
				newBaby.setSpecies(mother.getSpecies());
				PokemonProperties properties =
						PokemonProperties.Companion.parse(findForm(other.getForm()),
								" ", "=");
				properties.apply(newBaby);
				return newBaby;
			}
			return mother.clone(true, true);
		} else {
			Pokemon preEvolution = mother.getPreEvolution().getSpecies().create(1);
			PokemonProperties properties =
				PokemonProperties.Companion.parse(findForm(mother.getPreEvolution().getForm()),
						" ", "=");
		properties.apply(preEvolution);
			return findBaby(preEvolution, other);
		}
	}

	private static String findForm(FormData form) {
		switch (form.getName()) {
			case "Paldea":
				return "paldean";
			case "Alola":
				return "alolan";
			case "Galar":
				return "galarian";
			default:
				return "normal";
		}

	}

}
