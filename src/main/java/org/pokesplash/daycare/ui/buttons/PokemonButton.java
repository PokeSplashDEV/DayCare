package org.pokesplash.daycare.ui.buttons;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.pokesplash.daycare.util.CobblemonUtils;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class PokemonButton {
	public Button getButton(Pokemon pokemon, Consumer<ButtonAction> callback) {

		if (pokemon == null) {
			return GooeyButton.builder()
					.display(new ItemStack(Items.NETHER_STAR))
					.title("§dChoose A Pokemon")
					.onClick(callback)
					.build();
		}

		Collection<String> lore = new ArrayList<>();

		lore.add("§2Form: §a" + pokemon.getForm().getName());
		lore.add("§2Nature: §a" + Utils.capitaliseFirst(pokemon.getNature().getName().toString().split(":")[1]));

		if (CobblemonUtils.isHA(pokemon)) {
			lore.add("§2Ability: §a" + Utils.capitaliseFirst(pokemon.getAbility().getName()) + " §b(HA)");
		} else {
			lore.add("§2Ability: §a" + Utils.capitaliseFirst(pokemon.getAbility().getName()));
		}
		lore.add("§2Gender: §a" + Utils.capitaliseFirst(pokemon.getGender().toString()));
		lore.add("§2Ball: §a" + Utils.capitaliseFirst(pokemon.getCaughtBall().getName().toString().split(":")[1]));
		lore.add("§7Stats:");
		lore.add("§dHP §8- §3IV: §a" + pokemon.getIvs().get(Stats.HP));
		lore.add("§cAtk §8- §3IV: §a" + pokemon.getIvs().get(Stats.ATTACK));
		lore.add("§6Def §8- §3IV: §a" + pokemon.getIvs().get(Stats.DEFENCE));
		lore.add("§5SpAtk §8- §3IV: §a" + pokemon.getIvs().get(Stats.SPECIAL_ATTACK));
		lore.add("§eSpDef §8- §3IV: §a" + pokemon.getIvs().get(Stats.SPECIAL_DEFENCE));
		lore.add("§3Spe §8- §3IV: §a" + pokemon.getIvs().get(Stats.SPEED));
		lore.add("§6Moves:");
		for (Move move : pokemon.getMoveSet().getMoves()) {
			lore.add("§f" + move.getName());
		}

		return GooeyButton.builder()
				.display(PokemonItem.from(pokemon))
				.title(pokemon.getShiny() ? "§e" + pokemon.getSpecies().getName() :
						"§3" + pokemon.getSpecies().getName())
				.lore(lore)
				.onClick(callback)
				.build();
	}

}
