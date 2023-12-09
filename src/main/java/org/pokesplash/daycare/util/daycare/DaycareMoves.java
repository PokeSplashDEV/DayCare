package org.pokesplash.daycare.util.daycare;

import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;

import java.util.ArrayList;

public class DaycareMoves {
	public static ArrayList<Move> getMoves(Pokemon parent1, Pokemon parent2, Pokemon baby) {

		ArrayList<MoveTemplate> eggMoves = new ArrayList<>(baby.getForm().getMoves().getEggMoves());
		ArrayList<MoveTemplate> levelMoves = new ArrayList<>(baby.getForm().getMoves().getLevelUpMovesUpTo(100));
		ArrayList<MoveTemplate> tmMoves = new ArrayList<>(baby.getForm().getMoves().getTmMoves());

		Pokemon mother = DayCareUtils.getMother(parent1, parent2);
		Pokemon father = parent1.equals(mother) ? parent2 : parent1;

		ArrayList<Move> motherMoves = new ArrayList<>(mother.getMoveSet().getMoves());
		ArrayList<Move> fatherMoves = new ArrayList<>(father.getMoveSet().getMoves());

		ArrayList<Move> combinedMoves = new ArrayList<>();
		combinedMoves.addAll(motherMoves);
		combinedMoves.addAll(fatherMoves);

		ArrayList<Move> newMoves = new ArrayList<>();

		// Mother egg moves
		for (Move move : motherMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (eggMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		// Father egg moves
		for (Move move : fatherMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (eggMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		// TM moves
		for (Move move : combinedMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (tmMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		// Level Moves
		for (Move move : combinedMoves) {
			if (newMoves.size() >= 4) {
				break;
			}
			if (levelMoves.contains(move.getTemplate())) {
				newMoves.add(move);
			}
		}

		// Level 1 Moves
		if (newMoves.size() < 4) {
			ArrayList<MoveTemplate> level1Moves =
					new ArrayList<>(baby.getSpecies().getMoves().getLevelUpMovesUpTo(1));

			for (MoveTemplate move : level1Moves) {
				if (newMoves.size() >= 4) {
					break;
				}
				newMoves.add(move.create());
			}
		}
		return newMoves;
	}

	public static Pokemon teachEggMoves(Pokemon receiver, Pokemon sender) {

		if (receiver == null) {
			return null;
		}

		if (sender == null) {
			return receiver;
		}

		FormData baby = DayCareUtils.findLowestEvo(receiver.getForm());

		if (receiver.heldItem().getItem().equals(CobblemonItems.MIRROR_HERB)) {
			ArrayList<MoveTemplate> eggMoves = new ArrayList<>(baby.getMoves().getEggMoves());

			ArrayList<Move> senderMoves = new ArrayList<>(sender.getMoveSet().getMoves());

			ArrayList<Move> receiverMoves = new ArrayList<>(receiver.getMoveSet().getMoves());

			for (Move move : senderMoves) {
				if (eggMoves.contains(move.getTemplate())
						&& receiverMoves.size() < 4
						&& !receiverMoves.contains(move)
				) {
					receiverMoves.add(move);
				}
			}

			for (int x=0; x < receiverMoves.size(); x++) {
				receiver.getMoveSet().setMove(x, receiverMoves.get(x));
			}

			return receiver;
		} else {
			return receiver;
		}
	}
}
