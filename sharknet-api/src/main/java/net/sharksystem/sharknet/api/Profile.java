package net.sharksystem.sharknet.api;

import java.util.List;

/**
 * Created by timol on 12.05.2016.
 *
 * Interface represents the Profile-Functionality, in our case the personal contact
 */
public interface Profile {


	/**
	 * Returns the Contact of the Profile
	 * @return
     */
    public Contact getContact();

	/**
	 * Returns the Settings of the App
	 * @return
     */
    public Setting getSettings();

	/**
	 * returns a List of all Interests the profile is interested in
	 * @return
     */
    public List<String> getInterests();
}
