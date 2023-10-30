package org.pokesplash.daycare.event;

import org.pokesplash.daycare.event.events.AddPokemonEvent;
import org.pokesplash.daycare.event.obj.Event;

public class DayCareEvents {
	public static Event<AddPokemonEvent> ADD_POKEMON = new Event<>();
}
