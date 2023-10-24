package org.pokesplash.daycare.storage;

import org.pokesplash.daycare.DayCare;

import java.util.*;

public abstract class TimerStorage {
	private static HashMap<UUID ,Timer> timers = new HashMap<>();

	public static Timer getTimer(UUID id) {
		return timers.get(id);
	}

	public static void addTimer(IncubatorStorage incubator) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				timer.cancel();
			}
		}, incubator.getEndTime());

		timers.put(incubator.getId(), timer);
	}

	public static void removeTimer(UUID id) {
		Timer timer = timers.remove(id);
		timer.cancel();
	}

	public static void removeAllTimers() {
		for (UUID id : timers.keySet()) {
			removeTimer(id);
		}
	}


	public static void init() {
		removeAllTimers();

		for (PlayerStorage storage : DayCare.storage.getStorage().values()) {


			for (IncubatorStorage incubator : storage.getIncubators()) {

				if (incubator.getEndTime() > new Date().getTime()) {
					addTimer(incubator);
				}
			}
		}
	}
}
