package org.pokesplash.daycare.command;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.pokesplash.daycare.util.LuckPermsUtils;
import org.pokesplash.daycare.util.Utils;
import org.pokesplash.daycare.util.daycare.DayCareUtils;

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
								.executes(this::run)
								.then(CommandManager.literal("species")
										.executes(this::species))
								.then(CommandManager.literal("form")
										.executes(this::form))
								.then(CommandManager.literal("ball")
										.executes(this::ball))
								.then(CommandManager.literal("ability")
										.executes(this::ability))
								.then(CommandManager.literal("nature")
										.executes(this::nature))
								.then(CommandManager.literal("shiny")
										.executes(this::shiny))
								.then(CommandManager.literal("gender")
										.executes(this::gender))
								.then(CommandManager.literal("genderRatio")
										.executes(this::genderRatio))
								.then(CommandManager.literal("ivs")
										.executes(this::ivs))
								.then(CommandManager.literal("moves")
										.executes(this::moves))))
				.build();
	}

	public int moves(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		String moves = "\n";
		for (Move move : pokemon.getMoveSet().getMoves()) {
			moves += "§7" + move.getName() + "\n";
		}

		context.getSource().sendMessage(Text.literal(moves));

		return 1;
	}
	public int ivs(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		String ivs = "§4IVs: \n§cHP: §e" + pokemon.getIvs().get(Stats.HP) +
				"\n§cAtk: §e" + pokemon.getIvs().get(Stats.ATTACK) +
				"\n§cDef: §e" + pokemon.getIvs().get(Stats.DEFENCE) +
				"\n§cSpA: §e" + pokemon.getIvs().get(Stats.SPECIAL_ATTACK) +
				"\n§cSpD: §e" + pokemon.getIvs().get(Stats.SPECIAL_DEFENCE) +
				"\n§cSpe: §e" + pokemon.getIvs().get(Stats.SPEED);

		context.getSource().sendMessage(Text.literal(ivs));

		return 1;
	}
	public int gender(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		context.getSource().sendMessage(Text.literal("§3Gender: §c")
				.append(Text.translatable(pokemon.getGender().name()))
				.setStyle(Style.EMPTY.withColor(TextColor.parse("aqua"))));

		return 1;
	}

	public int genderRatio(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);


		int male = 0;
		int female = 0;
		int genderless = 0;
		for (int x=0; x < 10000; x++) {
			Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

			if (pokemon.getGender().equals(Gender.FEMALE)) {
				female ++;
				continue;
			}

			if (pokemon.getGender().equals(Gender.MALE)) {
				male ++;
				continue;
			}

			if (pokemon.getGender().equals(Gender.GENDERLESS)) {
				genderless ++;
				continue;
			}
		}

		context.getSource().sendMessage(Text.literal(
				"§3Male: §b" + male +
						"\n§5Female: §d" + female +
						"\n§8Genderless: §7" + genderless
		));

		return 1;
	}

	public int shiny(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		context.getSource().sendMessage(Text.literal("§6Shiny: §e" + pokemon.getShiny()));

		return 1;
	}
	public int nature(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		context.getSource().sendMessage(Text.literal("§4Nature: §c")
				.append(Text.translatable(pokemon.getNature().getDisplayName()))
						.setStyle(Style.EMPTY.withColor(TextColor.parse("red"))));

		return 1;
	}
	public int ability(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		try {
			ServerPlayerEntity player = context.getSource().getPlayer();

			int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
			int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

			PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

			Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

			context.getSource().sendMessage(Text.literal("§2Ability: §a")
					.append(Text.translatable(pokemon.getAbility().getDisplayName())
							.setStyle(Style.EMPTY.withColor(TextColor.parse("green")))));
		} catch (Exception e) {
			e.printStackTrace();
			context.getSource().sendMessage(Text.literal(e.getMessage()));
		}

		return 1;
	}
	public int ball(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		context.getSource().sendMessage(Text.literal("§1Ball: §9")
				.append(Text.translatable(pokemon.getCaughtBall().item().getName().getString())
						.setStyle(Style.EMPTY.withColor(TextColor.parse("blue")))));

		return 1;
	}
	public int form(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		context.getSource().sendMessage(Text.literal("§3Form: §b" + pokemon.getForm().getName()));

		return 1;
	}
	public int species(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);

		context.getSource().sendMessage(Text.literal("§5Species: §d" + pokemon.getSpecies().getName()));

		return 1;
	}

	public int run(CommandContext<ServerCommandSource> context) {

		if (!context.getSource().isExecutedByPlayer()) {
			context.getSource().sendMessage(Text.literal("This command must be executed by a player."));
		}

		ServerPlayerEntity player = context.getSource().getPlayer();

		int slot1 = IntegerArgumentType.getInteger(context, "slot1") - 1;
		int slot2 = IntegerArgumentType.getInteger(context, "slot2") - 1;

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		Pokemon pokemon = DayCareUtils.makeBaby(party.get(slot1), party.get(slot2), player, true);


		String ivs = "\n§4IVs: \n§cHP: §e" + pokemon.getIvs().get(Stats.HP) +
				"\n§cAtk: §e" + pokemon.getIvs().get(Stats.ATTACK) +
				"\n§cDef: §e" + pokemon.getIvs().get(Stats.DEFENCE) +
				"\n§cSpA: §e" + pokemon.getIvs().get(Stats.SPECIAL_ATTACK) +
				"\n§cSpD: §e" + pokemon.getIvs().get(Stats.SPECIAL_DEFENCE) +
				"\n§cSpe: §e" + pokemon.getIvs().get(Stats.SPEED);

		String moves = "\n";
		for (Move move : pokemon.getMoveSet().getMoves()) {
			moves += "§7" + move.getName() + "\n";
		}


		Text output = Text.literal("§5Species: §d" + pokemon.getSpecies().getName())
				.append(Text.literal("\n§3Form: §b" + pokemon.getForm().getName()))
				.append(Text.literal("\n§1Ball: "))
				.append(Text.translatable(pokemon.getCaughtBall().item().getName().getString())
						.setStyle(Style.EMPTY.withColor(TextColor.parse("blue"))))
				.append(Text.literal("\n§2Ability: §a" + pokemon.getAbility().getDisplayName()))
				.append(Text.literal("\n§4Nature: "))
				.append(Text.translatable(pokemon.getNature().getDisplayName())
						.setStyle(Style.EMPTY.withColor(TextColor.parse("red"))))
				.append("\n§6Shiny: §e" + pokemon.getShiny())
				.append("\n§3Gender: §b" + pokemon.getGender().name())
				.append(ivs).append(moves);

		context.getSource().sendMessage(output);

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
