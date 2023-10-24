package org.pokesplash.daycare;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pokesplash.daycare.command.CommandHandler;
import org.pokesplash.daycare.config.Config;
import org.pokesplash.daycare.config.Lang;
import org.pokesplash.daycare.storage.Storage;
import org.pokesplash.daycare.storage.TimerStorage;

public class DayCare implements ModInitializer {
	public static final String MOD_ID = "DayCare";
	public static final String BASE_PATH = "/config/" + MOD_ID + "/";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Config config = new Config();
	public static final Lang lang = new Lang();
	public static final Storage storage = new Storage();

	/**
	 * Runs the mod initializer.
	 */
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandHandler::registerCommands);
		load();

		ServerLifecycleEvents.SERVER_STOPPING.register((e) -> {
			TimerStorage.removeAllTimers();
		});
	}

	public static void load() {
		config.init();
		lang.init();
		storage.init();
		TimerStorage.init();
	}
}
