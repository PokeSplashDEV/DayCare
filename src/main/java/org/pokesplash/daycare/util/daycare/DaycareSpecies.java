package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;

public abstract class DaycareSpecies {
	public static Species getSpecies(Pokemon parentOne, Pokemon parentTwo) {

		boolean isVolbeatIllumise =
				BreedingExceptions.isVolbeatIllumise(parentOne) ||
						BreedingExceptions.isVolbeatIllumise(parentTwo);

		boolean isNidoran = BreedingExceptions.isNidoran(parentOne) ||
				BreedingExceptions.isNidoran(parentTwo);


		if(BreedingExceptions.hasDitto(parentOne, parentTwo))
		{
			if(isVolbeatIllumise)
			{
				return BreedingExceptions.getVolbeatIllumise();
			}
			if(isNidoran)
			{
				return BreedingExceptions.getNidoran();
			}
			// Gets the non-ditto parent and returns their species.
			return BreedingExceptions.getNonDitto(parentOne, parentTwo).getSpecies();
		}

		if (isVolbeatIllumise) {
			return BreedingExceptions.getVolbeatIllumise();
		}

		if (isNidoran) {
			return BreedingExceptions.getNidoran();
		}

		return DayCareUtils.getMother(parentOne, parentTwo).getSpecies();
	}
}