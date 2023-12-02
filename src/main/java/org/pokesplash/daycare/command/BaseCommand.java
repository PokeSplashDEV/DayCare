package org.pokesplash.daycare.command;

import ca.landonjw.gooeylibs2.api.UIManager;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.daycare.ui.MainMenu;
import org.pokesplash.daycare.util.LuckPermsUtils;

public class BaseCommand {
	public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		LiteralArgumentBuilder<ServerCommandSource> root = CommandManager
				.literal("daycare")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".base");
					} else {
						return true;
					}
				})
				.executes(this::run);

		LiteralCommandNode<ServerCommandSource> registeredCommand = dispatcher.register(root);

		registeredCommand.addChild(new ReloadCommand().build());
		registeredCommand.addChild(new DebugCommand().build());

	}

	public int run(CommandContext<ServerCommandSource> context) {
		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be ran by a player."));
		}

		PokemonBattle battle =
				Cobblemon.INSTANCE.getBattleRegistry().getBattleByParticipatingPlayer(context.getSource().getPlayer());

		if (battle != null) {
			context.getSource().sendMessage(Text.literal(
					"§cYou can not use DayCare while in a battle."
			));
			return 1;
		}

		ServerPlayerEntity player = context.getSource().getPlayer();
		UIManager.openUIForcefully(player, new MainMenu().getPage(player.getUuid()));
		return 1;
	}
}
