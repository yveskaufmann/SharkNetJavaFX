package net.sharksystem.sharknet.api;

import java.sql.Timestamp;

/**
 * Created by timol on 06.06.2016.
 */
public interface Timeable {
	/**
	 * The Interface is used by Messges, Feeds and Commands for easy sorting the lists
	 */

	public Timestamp getTimestamp();
}
