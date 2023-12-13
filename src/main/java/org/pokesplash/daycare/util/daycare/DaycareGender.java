package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;

public abstract class DaycareGender {
	public static Gender getGender(Pokemon baby) {
		float maleRatio = baby.getForm().getMaleRatio();

		if (maleRatio == -1) {
			return Gender.GENDERLESS;
		}

		if (Math.random() < maleRatio) {
			return Gender.MALE;
		} else {
			return Gender.FEMALE;
		}
	}
}
