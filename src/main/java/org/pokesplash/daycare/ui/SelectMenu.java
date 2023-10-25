package org.pokesplash.daycare.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.account.Incubator;
import org.pokesplash.daycare.ui.buttons.PokemonButton;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;

public class SelectMenu {
	public Page getPage(Incubator incubator, int parentSlot, ServerPlayerEntity player) {

		ArrayList<Button> pokemonButtons = new ArrayList<>();
		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		for (int x=0; x < 6; x++) {
			Pokemon pokemon = party.get(x);

			if (pokemon == null) {
				pokemonButtons.add(GooeyButton.builder()
						.display(new ItemStack(CobblemonItems.POKE_BALL))
						.title("§cNo Pokemon In Slot")
						.lore(new ArrayList<>())
						.hideFlags(FlagType.All)
						.build());
				continue;
			}

			pokemonButtons.add(new PokemonButton().getButton(pokemon, e -> {
				if (parentSlot == 1) {
					Pokemon oldPokemon = incubator.getParent1();
					incubator.setParent1(pokemon);
					changePokemon(oldPokemon, pokemon, party);
				} else {
					Pokemon oldPokemon = incubator.getParent2();
					incubator.setParent2(pokemon);
					changePokemon(oldPokemon, pokemon, party);
				}
				UIManager.openUIForcefully(e.getPlayer(), new IncubatorMenu().getPage(incubator));
			}));
		}

		pokemonButtons.add(GooeyButton.builder()
				.display(new ItemStack(Items.END_CRYSTAL))
				.title("§6Return Pokemon")
				.lore(new ArrayList<>())
				.onClick(e -> {
					if (parentSlot == 1) {
						Pokemon oldPokemon = incubator.getParent1();
						incubator.setParent1(null);
						changePokemon(oldPokemon, null, party);
					} else {
						Pokemon oldPokemon = incubator.getParent2();
						incubator.setParent2(null);
						changePokemon(oldPokemon, null, party);
					}
					UIManager.openUIForcefully(e.getPlayer(), new IncubatorMenu().getPage(incubator));
				})
				.hideFlags(FlagType.All)
				.build());

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(DayCare.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		PlaceholderButton placeholder = new PlaceholderButton();

		ChestTemplate template = ChestTemplate.builder(3)
				.rectangle(1, 1, 1, 7, placeholder)
				.fill(filler)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, pokemonButtons, null);
		page.setTitle(DayCare.lang.getTitle());

		return page;
	}

	private void changePokemon(Pokemon oldPokemon, Pokemon newPokemon, PlayerPartyStore party) {
		if (newPokemon != null) {
			party.remove(newPokemon);
		}

		if (oldPokemon != null) {
			party.add(oldPokemon);
		}
	}
}
