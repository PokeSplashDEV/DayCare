package org.pokesplash.daycare.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.account.Incubator;
import org.pokesplash.daycare.event.DayCareEvents;
import org.pokesplash.daycare.event.events.RetrieveEggEvent;
import org.pokesplash.daycare.ui.buttons.PokemonButton;
import org.pokesplash.daycare.util.DayCareUtils;
import org.pokesplash.daycare.util.IllegalPokemonException;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.Date;

public class IncubatorMenu {
	public Page getPage(Incubator incubator) {
		Button parent1 = new PokemonButton().getButton(incubator.getParent1(), e -> {
			if (!incubator.isInProgress()) {
				UIManager.openUIForcefully(e.getPlayer(), new SelectMenu()
						.getPage(incubator, 1, e.getPlayer()));
			}
		});

		Button parent2 = new PokemonButton().getButton(incubator.getParent2(), e -> {
			if (!incubator.isInProgress()) {
				UIManager.openUIForcefully(e.getPlayer(), new SelectMenu()
						.getPage(incubator, 2, e.getPlayer()));
			}
		});


		Button baby = makeBabyButton(incubator);

		Button confirm = GooeyButton.builder()
				.display(new ItemStack(Items.GREEN_STAINED_GLASS_PANE))
				.title("§aConfirm")
				.onClick(e -> {
					if (incubator.getBaby() == null) {
						incubator.setBaby(DayCareUtils.makeBaby(incubator.getParent1(), incubator.getParent2(),
								e.getPlayer()));
						incubator.setEndTime(new Date().getTime() +
								((long) DayCare.config.getIncubationTime() * 60 * 1000));
					}
					incubator.setInProgress(true);
					UIManager.openUIForcefully(e.getPlayer(), new IncubatorMenu().getPage(incubator));
				})
				.build();

		Button cancel = GooeyButton.builder()
				.display(new ItemStack(Items.RED_STAINED_GLASS_PANE))
				.title("§cCancel")
				.onClick(e -> {
					incubator.setInProgress(false);
					if (incubator.getEndTime() > new Date().getTime()) {
						incubator.setBaby(null);
					}
					UIManager.openUIForcefully(e.getPlayer(), new IncubatorMenu().getPage(incubator));
				})
				.build();

		String requiredString = "";
		boolean compatible = false;
		try {
			compatible = DayCareUtils.isCompatible(incubator.getParent1(), incubator.getParent2());
		} catch (IllegalPokemonException e) {
			requiredString = e.getMessage();
		}

		Button required = GooeyButton.builder()
				.display(new ItemStack(Items.ORANGE_STAINED_GLASS_PANE))
				.title("§6" + requiredString)
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
		} else if (incubator.getParent1() == null || incubator.getParent2() == null
		|| !compatible) {
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
		if (incubator.getBaby() == null) {

			return GooeyButton.builder()
					.display(new ItemStack(Items.BARRIER))
					.title("§cNo Egg Available")
					.build();

		} else if (incubator.getEndTime() > new Date().getTime()) {
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
						PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(e.getPlayer());

						DayCareEvents.RETRIEVE_EGG.trigger(
								new RetrieveEggEvent(e.getPlayer(), incubator, incubator.getBaby())
						);

						party.add(incubator.getBaby());
						if (incubator.isInProgress()) {
							incubator.setBaby(DayCareUtils.makeBaby(incubator.getParent1(), incubator.getParent2(),
									e.getPlayer()));
							incubator.setEndTime(new Date().getTime() +
									((long) DayCare.config.getIncubationTime() * 60 * 1000));
						} else {
							incubator.setBaby(null);
							incubator.setEndTime(-1);
						}
						UIManager.openUIForcefully(e.getPlayer(), new IncubatorMenu().getPage(incubator));
					})
					.build();
		}


	}
}
