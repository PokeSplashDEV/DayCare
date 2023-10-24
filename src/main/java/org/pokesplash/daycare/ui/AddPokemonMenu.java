package org.pokesplash.daycare.ui;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.item.ItemStack;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.ui.buttons.PokemonButton;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddPokemonMenu {
	public void getPage(PlayerPartyStore party) {
		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(DayCare.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		List<Button> pokemon = new ArrayList<>(); // Creates a list for the Pokemon buttons.
		for (int x=0; x < 6; x++) { // Checks each slot.
			Pokemon mon = party.get(x);

			// If no Pokemon, add a Pokeball as a placeholder.
			if (mon == null) {
				Button ball = GooeyButton.builder()
						.display(new ItemStack(CobblemonItems.POKE_BALL, 1))
						.title("No Pokemon in Slot")
						.build();

				pokemon.add(ball);
				continue;
			}

			// Add the button to the list.
			pokemon.add(new PokemonButton().getButton(mon, null));
		}
	}
}
