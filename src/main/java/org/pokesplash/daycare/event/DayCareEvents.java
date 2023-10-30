package org.pokesplash.daycare.event;

import org.pokesplash.daycare.event.events.AddPokemonEvent;
import org.pokesplash.daycare.event.events.CreateEggEvent;
import org.pokesplash.daycare.event.events.RemovePokemonEvent;
import org.pokesplash.daycare.event.events.RetrieveEggEvent;
import org.pokesplash.daycare.event.obj.Event;

public class DayCareEvents {
	public static Event<AddPokemonEvent> ADD_POKEMON = new Event<>();
	public static Event<RemovePokemonEvent> REMOVE_POKEMON = new Event<>();
	public static Event<CreateEggEvent> CREATE_EGG = new Event<>();
	public static Event<RetrieveEggEvent> RETRIEVE_EGG = new Event<>();
}
