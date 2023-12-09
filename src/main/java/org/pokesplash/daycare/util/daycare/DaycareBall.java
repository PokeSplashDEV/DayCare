package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;

public class DaycareBall {
	public static PokeBall getBall(Pokemon parent1, Pokemon parent2, Pokemon baby) {

		boolean isVolbeatIllumise =
				BreedingExceptions.isVolbeatIllumise(parent1) ||
						BreedingExceptions.isVolbeatIllumise(parent2);

		boolean isNidoran = BreedingExceptions.isNidoran(parent1) ||
				BreedingExceptions.isNidoran(parent2);

		if(BreedingExceptions.hasDitto(parent1, parent2))
		{
			if(isVolbeatIllumise)
			{
				return BreedingExceptions.getVolbeatIllumiseParent(parent1, parent2).getCaughtBall();
			}
			if(isNidoran)
			{
				return BreedingExceptions.getNidoranParent(parent1, parent2).getCaughtBall();
			}
			// Gets the non-ditto parent and returns their species.
			return BreedingExceptions.getNonDitto(parent1, parent2).getCaughtBall();
		}

		ArrayList<PokeBall> validBalls = new ArrayList<>();
		if (BreedingExceptions.matchesPokemon(parent1, baby)) {
			validBalls.add(parent1.getCaughtBall());
		}

		if (BreedingExceptions.matchesPokemon(parent2, baby)) {
			validBalls.add(parent2.getCaughtBall());
		}

		return Utils.getRandomValue(validBalls);
	}
}
