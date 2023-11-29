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
import com.cobblemon.mod.common.CobblemonItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.account.Account;
import org.pokesplash.daycare.account.AccountProvider;
import org.pokesplash.daycare.account.Incubator;
import org.pokesplash.daycare.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class MainMenu {
	public Page getPage(UUID player) {
		Account account = AccountProvider.getAccount(player);

		ArrayList<Button> incubatorButtons = new ArrayList<>();
		for (UUID key : account.getIncubators().keySet()) {
			Incubator incubator = account.getIncubator(key);

			Item displayItem;

			boolean isEggReady =
					incubator.getEndTime() < new Date().getTime()
							&& incubator.getEndTime() != -1
							&& incubator.getBaby() != null;
			if (incubator.getEndTime() == -1 || incubator.getBaby() == null) {
				displayItem = CobblemonItems.DESTINY_KNOT;
			} else if (!isEggReady) {
				displayItem = Items.EGG;
			} else {
				displayItem = Items.TURTLE_EGG;
			}

			Collection<String> lore = new ArrayList<>();
			lore.add("§bParent 1: " + (incubator.getParent1() == null ? "None" :
					incubator.getParent1().getDisplayName().getString()));
			lore.add("§bParent 2: " + (incubator.getParent2() == null ? "None" :
					incubator.getParent2().getDisplayName().getString()));
			lore.add("§dEgg Available: " + (isEggReady ? "Yes" : "No"));

			if (!isEggReady && incubator.getEndTime() != -1) {
				lore.add("§aEnds in: " + Utils.parseLongDate(incubator.getEndTime() - new Date().getTime()));
			}

			incubatorButtons.add(
					GooeyButton.builder()
							.display(new ItemStack(displayItem))
							.title("§3§lClick To View Incubator")
							.lore(lore)
							.onClick(e -> {
								UIManager.openUIForcefully(e.getPlayer(), new IncubatorMenu().getPage(incubator));
							})
							.build());
		}

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(DayCare.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		int rows = (incubatorButtons.size() / 7) > 4 ? 6 : ((int) Math.ceil((double) incubatorButtons.size() / 7) + 2);


		PlaceholderButton placeholder = new PlaceholderButton();

		ChestTemplate template = ChestTemplate.builder(rows)
				.rectangle(1, 1, rows - 2, 7, placeholder)
				.fill(filler)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, incubatorButtons, null);
		page.setTitle("§3§lDayCare");

		return page;
	}
}
