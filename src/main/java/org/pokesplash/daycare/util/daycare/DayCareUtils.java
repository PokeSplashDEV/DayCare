package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.egg.EggGroup;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.*;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.event.DayCareEvents;
import org.pokesplash.daycare.event.events.CreateEggEvent;
import org.pokesplash.daycare.util.IllegalPokemonException;

import java.util.ArrayList;
import java.util.HashMap;

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

	public static Pokemon makeBaby(Pokemon parent1, Pokemon parent2, ServerPlayerEntity player, boolean isDebug) {
		Pokemon baby = new Pokemon().initialize();

		// Sets the species.
		baby.setSpecies(DaycareSpecies.getSpecies(parent1, parent2));

		// Sets form
		PokemonProperties properties =
				PokemonProperties.Companion.parse(DaycareForm.getForm(parent1, parent2, baby), " ", "=");
		properties.apply(baby);

		baby.setGender(DaycareGender.getGender(baby));

		// Sets Pokeball.
		PokeBall ball = DaycareBall.getBall(parent1, parent2, baby);
		if (ball.equals(PokeBalls.INSTANCE.getMASTER_BALL()) ||
				ball.equals(PokeBalls.INSTANCE.getCHERISH_BALL())) {
			ball = PokeBalls.INSTANCE.getPOKE_BALL();
		}
		baby.setCaughtBall(ball);

		// Sets Ability.
		baby.setAbility(DaycareAbility.getAbility(parent1, parent2, baby));

		// Sets nature if estone being held, or don't change.
		Nature newNature = DaycareNature.getNature(parent1, parent2);
		if (newNature != null) {
			baby.setNature(newNature);
		}

		// Sets IVs
		HashMap<Stat, Integer> stats = DaycareIVs.getIVs(parent1, parent2);
		for (Stat stat : stats.keySet()) {
			baby.getIvs().set(stat, stats.get(stat));
		}

		// Sets Moves
		baby.getMoveSet().clear();
		ArrayList<Move> moves = DaycareMoves.getMoves(parent1, parent2, baby);
		for (int x = 0; x < moves.size(); x++) {
			baby.getMoveSet().setMove(x, moves.get(x));
		}

		// TODO Shinies? Future?
		// TODO command to set shiny charm?

		if (!isDebug) {
			DayCareEvents.CREATE_EGG.trigger(
					new CreateEggEvent(player, parent1, parent2, baby));
		}

		return baby;
	}

	public static FormData findLowestEvo(FormData pokemon) {
		if (pokemon.getPreEvolution() == null) {
			return pokemon;
		} else {
			return findLowestEvo(pokemon.getPreEvolution().getForm());
		}
	}

	public static Pokemon getMother(Pokemon parent1, Pokemon parent2) {
		Species ditto = PokemonSpecies.INSTANCE.getByPokedexNumber(132, Cobblemon.MODID);

		if (parent1.getSpecies().equals(ditto)) {
			return parent2;
		} else if (parent2.getSpecies().equals(ditto)) {
			return parent1;
		} else if (parent1.getGender().equals(Gender.FEMALE)) {
			return parent1;
		}  else {
			return parent2;
		}
	}
}
