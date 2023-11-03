package org.pokesplash.daycare.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.pokesplash.daycare.util.DayCareUtils;
import org.pokesplash.daycare.util.LuckPermsUtils;
import org.pokesplash.daycare.util.Utils;

public class DebugCommand {
	public LiteralCommandNode<ServerCommandSource> build() {
		return CommandManager.literal("check")
				.requires(ctx -> {
					if (ctx.isExecutedByPlayer()) {
						return LuckPermsUtils.hasPermission(ctx.getPlayer(), CommandHandler.basePermission + ".check");
					} else {
						return true;
					}
				})
				.executes(this::usage)
				.then(CommandManager.argument("slot1", IntegerArgumentType.integer())
						.suggests((ctx, builder) -> {
							for (int x=1; x <= 6; x++) {
								builder.suggest(x);
							}
							return builder.buildFuture();
						})
						.executes(this::usage)
						.then(CommandManager.argument("slot2", IntegerArgumentType.integer())
								.suggests((ctx, builder) -> {
									for (int x=1; x <= 6; x++) {
										builder.suggest(x);
									}
									return builder.buildFuture();
								})
								.executes(this::run)))
				.build();
	}

	public int run(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		context.getSource().sendMessage(Text.literal(
				DayCareUtils.debugCompatible(party.get(slot1), party.get(slot2))
		));

		return 1;
	}

	public int usage(CommandContext<ServerCommandSource> context) {

		String usage = "§3DayCare Usage:\n§b- check <pokemon> <pokemon>";

		context.getSource().sendMessage(Text.literal(
				Utils.formatMessage(usage, context.getSource().isExecutedByPlayer())
		));

		return 1;
	}
}
