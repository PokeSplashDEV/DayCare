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
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
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

		Collection<Text> lore = new ArrayList<>();

		Style green = Text.empty().getStyle().withColor(TextColor.parse("green"));

		lore.add(Text.literal("§2Form: §a" + pokemon.getForm().getName()));
		lore.add(Text.literal("§2Nature: ").
				append(Text.translatable(pokemon.getNature().getDisplayName()).setStyle(green)));
		if (CobblemonUtils.isHA(pokemon)) {
			lore.add(Text.literal("§2Ability: ")
					.append(Text.translatable(pokemon.getAbility().getDisplayName()).setStyle(green))
					.append(Text.literal(" §b(HA)")));
		} else {
			lore.add(Text.literal("§2Ability: ")
					.append(Text.translatable(pokemon.getAbility().getDisplayName()).setStyle(green)));
		}
		lore.add(Text.literal("§2Gender: §a" + Utils.capitaliseFirst(pokemon.getGender().toString())));
		lore.add(Text.literal("§2Ball: ")
				.append(Text.translatable(pokemon.getCaughtBall().item().getName().getString()).setStyle(green)));

		lore.add(Text.literal("§2Item: §a" + (pokemon.heldItem().getItem().equals(Items.AIR) ? "None" :
				pokemon.heldItem().getName().getString())));
		lore.add(Text.literal("§7Stats:"));
		lore.add(Text.literal("§dHP §8- §3IV: §a" + (pokemon.getIvs().get(Stats.HP) == null ? 0 :
				pokemon.getIvs().get(Stats.HP))));
		lore.add(Text.literal("§cAtk §8- §3IV: §a" + (pokemon.getIvs().get(Stats.ATTACK) == null ? 0 :
				pokemon.getIvs().get(Stats.ATTACK))));
		lore.add(Text.literal("§6Def §8- §3IV: §a" + (pokemon.getIvs().get(Stats.DEFENCE) == null ? 0 :
				pokemon.getIvs().get(Stats.DEFENCE))));
		lore.add(Text.literal("§5SpAtk §8- §3IV: §a" + (pokemon.getIvs().get(Stats.SPECIAL_ATTACK) == null ? 0 :
				pokemon.getIvs().get(Stats.SPECIAL_ATTACK))));
		lore.add(Text.literal("§eSpDef §8- §3IV: §a" + (pokemon.getIvs().get(Stats.SPECIAL_DEFENCE) == null ? 0 :
				pokemon.getIvs().get(Stats.SPECIAL_DEFENCE))));
		lore.add(Text.literal("§3Spe §8- §3IV: §a" + (pokemon.getIvs().get(Stats.SPEED) == null ? 0 :
				pokemon.getIvs().get(Stats.SPEED))));
		lore.add(Text.literal("§6Moves:"));
		Style white = Text.empty().getStyle().withColor(TextColor.parse("white"));
		for (Move move : pokemon.getMoveSet().getMoves()) {
			lore.add(Text.translatable(move.getTemplate().getDisplayName().getString()).setStyle(white));
		}

		return GooeyButton.builder()
				.display(PokemonItem.from(pokemon))
				.title(pokemon.getShiny() ? "§e" + pokemon.getSpecies().getName() :
						"§3" + pokemon.getSpecies().getName())
				.lore(Text.class, lore)
				.onClick(callback)
				.build();
	}

}
