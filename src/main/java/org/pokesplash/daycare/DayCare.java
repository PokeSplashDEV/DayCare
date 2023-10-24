package org.pokesplash.daycare;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.daycare.account.AccountProvider;
import org.pokesplash.daycare.command.CommandHandler;
import org.pokesplash.daycare.config.Config;
import org.pokesplash.daycare.config.Lang;

public class DayCare implements ModInitializer {
	public static final String MOD_ID = "DayCare";
	public static final String BASE_PATH = "/config/" + MOD_ID + "/";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Config config = new Config();
	public static final Lang lang = new Lang();

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		load();

		ServerLifecycleEvents.SERVER_STOPPING.register((e) -> {
			// Remove all timers.
		});
	}

	public static void load() {
		config.init();
		lang.init();
		AccountProvider.init();
	}
}
