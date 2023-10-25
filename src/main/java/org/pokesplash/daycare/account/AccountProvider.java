package org.pokesplash.daycare.account;

import com.google.gson.Gson;
import org.pokesplash.daycare.DayCare;
import org.pokesplash.daycare.util.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AccountProvider {
	private static HashMap<UUID, Account> accounts;

	public static Account getAccount(UUID id) {
		if (accounts.get(id) == null) {
			accounts.put(id, new Account(id));
		}
		return accounts.get(id);
	}

	public static void init() {
		accounts = new HashMap<>();

		File dir = Utils.checkForDirectory(DayCare.BASE_PATH + "accounts/");

		String[] list = dir.list();

		if (list.length == 0) {
			return;
		}

		for (String file : list) {
			CompletableFuture<Boolean> futureRead = Utils.readFileAsync(DayCare.BASE_PATH + "accounts/", file, e -> {
				Gson gson = Utils.newGson();
				Account account = gson.fromJson(e, Account.class);
				account.updateIncubators();
				accounts.put(account.getOwner(), account);

				for (UUID incubator : account.getIncubators().keySet()) {
					Incubator inc = account.getIncubator(incubator);
				}
			});

			if (!futureRead.join()) {
				DayCare.LOGGER.error("Unable to read DayCare file: " + file);
			}
		}
	}
}
