package org.pokesplash.daycare.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import jdk.jshell.execution.Util;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.account.Incubator;
import org.pokesplash.daycare.ui.buttons.PokemonButton;
import org.pokesplash.daycare.util.DayCareUtils;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.Date;

public class IncubatorMenu {
	public Page getPage(Incubator incubator) {
		Button parent1 = new PokemonButton().getButton(incubator.getParent1(), null); // TODO callback

		Button parent2 = new PokemonButton().getButton(incubator.getParent2(), null); // TODO callback


		Button baby = makeBabyButton(incubator);

		Button confirm = GooeyButton.builder()
				.display(new ItemStack(Items.GREEN_STAINED_GLASS_PANE))
				.title("§aConfirm")
				.onClick(e -> {
					// TODO all logic to create baby, lock in Pokemon, set incubator to in progress.
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(new ItemStack(Items.RED_STAINED_GLASS_PANE))
				.title("§cCancel")
				.onClick(e -> {
					// TODO cancel breeding, remove timer, cancel baby, return parents.
				})
				.build();

		Button required = GooeyButton.builder()
				.display(new ItemStack(Items.ORANGE_STAINED_GLASS_PANE))
				.title("§6Please select two Pokemon to breed.")
				.build();

		Button back = GooeyButton.builder()
				.display(new ItemStack(Items.END_CRYSTAL))
				.title("§6Return To Main Menu.")
				.onClick(e -> {
					UIManager.openUIForcefully(e.getPlayer(), new MainMenu().getPage(e.getPlayer().getUuid()));
				})
				.build();

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(DayCare.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(filler)
				.set(10, back)
				.set(11, parent1)
				.set(12, parent2)
				.set(14, baby);

		if (incubator.isInProgress()) {
			template.set(16, cancel);
		} else if (incubator.getParent1() == null || incubator.getParent2() == null) {
			template.set(16, required);
		} else {
			template.set(16, confirm);
		}

		return GooeyPage.builder()
				.template(template.build())
				.title("§3Incubator")
				.build();

	}

	private Button makeBabyButton(Incubator incubator) {
		if (incubator.getParent1() == null || incubator.getParent2() == null) {

			return GooeyButton.builder()
					.display(new ItemStack(Items.BARRIER))
					.title("§cNo Egg Available")
					.build();

		} else if (incubator.isInProgress()) {
			return GooeyButton.builder()
					.display(new ItemStack(Items.EGG))
					.title("§6Time Until Completion: " +
							Utils.parseLongDate(incubator.getEndTime() - new Date().getTime()))
					.build();
		} else {
			return GooeyButton.builder()
					.display(new ItemStack(Items.TURTLE_EGG))
					.title("§bClick To Redeem Your Egg!")
					.onClick(e -> {
						DayCareUtils.removeBabyFromIncubator(incubator);
						// TODO give person egg, create new Baby with timer.
					})
					.build();
		}


	}
}
