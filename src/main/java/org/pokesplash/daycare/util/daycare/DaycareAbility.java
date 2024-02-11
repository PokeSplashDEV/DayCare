package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.abilities.AbilityTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import org.pokesplash.daycare.util.CobblemonUtils;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DaycareAbility {
	public static Ability getAbility(Pokemon parent1, Pokemon parent2, Pokemon baby) {
		Pokemon mother = DayCareUtils.getMother(parent1, parent2);

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
}
