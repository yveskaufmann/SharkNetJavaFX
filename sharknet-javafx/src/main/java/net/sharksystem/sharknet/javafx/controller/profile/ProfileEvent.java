package net.sharksystem.sharknet.javafx.controller.profile;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;
import net.sharksystem.sharknet.api.Profile;

import java.io.Serializable;

/**
 * A event which indicates changes to a profile.
 *
 * @author Yves Kaufmann
 * @since 15.07.2016
 */
public class ProfileEvent extends Event {

	private static final long serialVersionUID = 20121324324107L;

	/**
	 * Common supertype for all profile event types.
	 */
	public static final EventType<ProfileEvent> ANY =
		new EventType<>(Event.ANY, "PROFILE");

	public static final EventType<ProfileEvent> CHANGED =
		new EventType<>(ANY, "PROFILE_CHANGED");

	private Profile profile;

	public ProfileEvent(@NamedArg("eventType") EventType<? extends Event> eventType, Profile profile) {
		super(eventType);
		this.profile = profile;
	}

	public Profile getProfile() {
		return profile;
	}

	public static ProfileEvent any(Profile profile) {
		return new ProfileEvent(ANY, profile);
	}

	public static ProfileEvent changed(Profile profile) {
		return new ProfileEvent(CHANGED, profile);
	}
}
