package net.sharksystem.sharknet.api;

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
	public void setContact(Contact c);

	/**
	 * Returns the Settings of the App
	 * @return
     */
    public Setting getSettings();

	/**
	 * deletes a Profile
	 */
	public void delete();

	/**
	 * Safes the Profile in the KB
	 */
	public void save();

	public void update();

	/**
	 * Returns the personal configured schedule week.
	 * @return
	 */
	public ScheduleWeek getScheduleWeek();

	/**
	 * Set the personal configured schedule week.
	 * @param scheduleWeek
     */
	public void setScheduleWeek(ScheduleWeek scheduleWeek);

	/**
	 * Method for the Login
	 */

	public boolean login(String password);
	/**
	 * Method to set password
	 */
	public void setPassword(String password);

	/**
	 * Returns true if profiles are equal
	 * @param p
	 * @return
     */
	public boolean isEqual(Profile p);

	/**
	 * Returns the Blacklist of the Profile
	 * @return
     */
	public Blacklist getBlacklist();

	/**
	 * Generates new pair of keys
	 */
	public void renewKeys();
}
