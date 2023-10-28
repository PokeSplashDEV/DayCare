package org.pokesplash.daycare.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.abilities.AbilityTemplate;
import com.cobblemon.mod.common.api.data.JsonDataRegistry;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.egg.EggGroup;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.*;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.system.windows.POINT;
import org.pokesplash.daycare.DayCare;

import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

		// Sets Pokeball.
		baby.setCaughtBall(getBall(parent1, parent2, baby));

		// Sets Ability.
		baby.setAbility(getAbility(parent1, parent2, baby));

		// Sets nature if estone being held, or don't change.
		Nature newNature = getNature(parent1, parent2);
		if (newNature != null) {
			baby.setNature(newNature);
		}

		// Sets IVs
		HashMap<Stat, Integer> stats = getIVs(parent1, parent2);
		for (Stat stat : stats.keySet()) {
			baby.getIvs().set(stat, stats.get(stat));
		}

		// Sets Moves
		ArrayList<Move> moves = getMoves(parent1, parent2, baby);
		for (int x=0; x < moves.size(); x++) {
			baby.getMoveSet().setMove(x, moves.get(x));
		}

		// TODO Shinies? Future?
		// TODO command to set shiny charm?


		return baby;
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

	private static PokeBall getBall(Pokemon parent1, Pokemon parent2, Pokemon baby) {
		Species babyParent1 = findLowestEvo(parent1.getSpecies());
		Species babyParent2 = findLowestEvo(parent2.getSpecies());

		ArrayList<PokeBall> validBalls = new ArrayList<>();
		if (babyParent1.equals(baby.getSpecies())) {
			validBalls.add(parent1.getCaughtBall());
		}

		if (babyParent2.equals(baby.getSpecies())) {
			validBalls.add(parent2.getCaughtBall());
		}

		for (PokeBall ball : validBalls) {
			if (ball.equals(PokeBalls.INSTANCE.getMASTER_BALL()) ||
					ball.equals(PokeBalls.INSTANCE.getCHERISH_BALL())) {
				validBalls.remove(ball);
				validBalls.add(PokeBalls.INSTANCE.getPOKE_BALL());
			}
		}

		return Utils.getRandomValue(validBalls);
	}

	private static Ability getAbility(Pokemon parent1, Pokemon parent2, Pokemon baby) {
		Pokemon mother = getMother(parent1, parent2);

		int randomNumber = ThreadLocalRandom.current().nextInt(1, 11);

		ArrayList<AbilityTemplate> regularAbilities = CobblemonUtils.getNormalAbilities(baby);
		ArrayList<AbilityTemplate> motherAbilities = CobblemonUtils.getNormalAbilities(mother);

		AbilityTemplate ha = CobblemonUtils.getHA(baby);

		Ability newAbility;

		if (CobblemonUtils.isHA(mother)) {
			if (randomNumber < 7) {
				newAbility = ha == null ? Utils.getRandomValue(regularAbilities).create(true) : ha.create(true);
			} else {
				newAbility = Utils.getRandomValue(regularAbilities).create(true);
			}
		} else {
			// 80% change to give same ability.
			if (randomNumber < 9) {
				return regularAbilities.get(motherAbilities.indexOf(mother.getAbility().getTemplate())).create(true);
			} else {
				if (regularAbilities.size() > 1) {
					regularAbilities.remove(motherAbilities.indexOf(mother.getAbility().getTemplate()));
					newAbility = Utils.getRandomValue(regularAbilities).create(true);
				} else {
					newAbility = Utils.getRandomValue(regularAbilities).create(true);
				}
			}
		}

		return newAbility;
	}

	private static Nature getNature(Pokemon parent1, Pokemon parent2) {

		if (parent1.heldItem().getItem().equals(CobblemonItems.EVERSTONE)) {
			return parent1.getNature();
		} else if (parent2.heldItem().getItem().equals(CobblemonItems.EVERSTONE)) {
			return parent2.getNature();
		} else {
			return null;
		}
	}

	private static HashMap<Stat, Integer> getIVs(Pokemon parent1, Pokemon parent2) {
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
				newStats.put(stat, Integer.valueOf(parent.getIvs().get(stat)));
				baseStats.remove(stat);
			}
		} else {
			for (int x=newStats.size(); x<3; x++) {
				Stat stat = Utils.getRandomValue(baseStats);
				Pokemon parent = Utils.getRandomValue(Arrays.asList(parent1, parent2));
				newStats.put(stat, Integer.valueOf(parent.getIvs().get(stat)));
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

	private static ArrayList<Move> getMoves(Pokemon parent1, Pokemon parent2, Pokemon baby) {

		ArrayList<MoveTemplate> eggMoves = new ArrayList<>(baby.getSpecies().getMoves().getEggMoves());
		ArrayList<MoveTemplate> levelMoves = new ArrayList<>(baby.getSpecies().getMoves().getLevelUpMovesUpTo(100));
		ArrayList<MoveTemplate> tmMoves = new ArrayList<>(baby.getSpecies().getMoves().getTmMoves());

		Pokemon mother = getMother(parent1, parent2);
		Pokemon father = parent1.equals(mother) ? parent2 : parent1;

		ArrayList<Move> motherMoves = new ArrayList<>(mother.getMoveSet().getMoves());
		ArrayList<Move> fatherMoves = new ArrayList<>(father.getMoveSet().getMoves());

		ArrayList<Move> combinedMoves = new ArrayList<>();
		combinedMoves.addAll(motherMoves);
		combinedMoves.addAll(fatherMoves);

		ArrayList<Move> newMoves = new ArrayList<>();

		// Mother egg moves
		for (Move move : motherMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (eggMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		// Father egg moves
		for (Move move : fatherMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (eggMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		// TM moves
		for (Move move : combinedMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (tmMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		// Level Moves
		for (Move move : combinedMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (levelMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		return newMoves;
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

	private static Pokemon getMother(Pokemon parent1, Pokemon parent2) {
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

	public static Pokemon teachEggMoves(Pokemon receiver, Pokemon sender) {

		if (receiver == null) {
			return null;
		}

		if (sender == null) {
			return receiver;
		}

		if (receiver.heldItem().getItem().equals(CobblemonItems.MIRROR_HERB)) {
			ArrayList<MoveTemplate> eggMoves = new ArrayList<>(receiver.getSpecies().getMoves().getEggMoves());

			ArrayList<Move> senderMoves = new ArrayList<>(sender.getMoveSet().getMoves());

			ArrayList<Move> receiverMoves = new ArrayList<>(receiver.getMoveSet().getMoves());

			for (Move move : senderMoves) {
				if (eggMoves.contains(move.getTemplate())
						&& receiverMoves.size() < 4
						&& !receiverMoves.contains(move)
				) {
					receiverMoves.add(move);
				}
			}

			for (int x=0; x < receiverMoves.size(); x++) {
				receiver.getMoveSet().setMove(x, receiverMoves.get(x));
			}

			return receiver;
		} else {
			return receiver;
		}
	}
}
