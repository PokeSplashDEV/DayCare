package org.pokesplash.daycare.account;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.JsonObject;
import org.pokesplash.daycare.util.daycare.DayCareUtils;
import org.pokesplash.daycare.util.daycare.DaycareMoves;

import java.util.UUID;

public class Incubator {
	private UUID owner;
	private UUID id;
	private JsonObject parent1;
	private JsonObject parent2;
	private JsonObject baby;
	private long endTime;
	private boolean inProgress;

	public Incubator(UUID owner) {
		this.owner = owner;
		id = UUID.randomUUID();
		reset();
	}

	public UUID getId() {
		return id;
	}

	public Pokemon getParent1() {
		if (parent1 == null) {
			return null;
		} else {
			return new Pokemon().loadFromJSON(parent1);
		}
	}

	public void setParent1(Pokemon parent1) {
		this.parent1 = parent1 == null ? null : parent1.saveToJSON(new JsonObject());
		update();
	}

	public Pokemon getParent2() {
		if (parent2 == null) {
			return null;
		} else {
			return new Pokemon().loadFromJSON(parent2);
		}
	}

	public void setParent2(Pokemon parent2) {
		this.parent2 = parent2 == null ? null : parent2.saveToJSON(new JsonObject());
		update();
	}

	public Pokemon getBaby() {
		if (baby == null) {
			return null;
		} else {
			return new Pokemon().loadFromJSON(baby);
		}
	}

	public void setBaby(Pokemon baby) {
		this.baby = baby == null ? null : baby.saveToJSON(new JsonObject());
		update();
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
		update();
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
		update();
	}

	public void reset() {
		parent1 = null;
		parent2 = null;
		baby = null;
		endTime = -1;
	}

	private void update() {
		Pokemon newParent1 = DaycareMoves.teachEggMoves(getParent1(), getParent2());
		this.parent1 = newParent1 == null ? null : newParent1.saveToJSON(new JsonObject());
		Pokemon newParent2 = DaycareMoves.teachEggMoves(getParent2(), getParent1());
		this.parent2 = newParent2 == null ? null : newParent2.saveToJSON(new JsonObject());
		AccountProvider.getAccount(owner).updateIncubator(this);
	}
}
