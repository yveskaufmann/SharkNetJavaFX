package net.sharksystem.sharknet.api;

import java.awt.*;

/**
 * Created by Thilo S. on 01.08.2016.
 */
public interface Lesson {

	String getLessonName();
	void setLessonName(String lessonName);

	String getAcronym();
	void setAcronym(String acronym);

	Color getColor();
	void setColor(Color color);

	String getContact();
	void setContact(String contact);

	String getContactTelephoneNumber();
	void setContactTelephoneNumber(String telephoneNumber);

	String getContactMailAddress();
	void setContactMailAddress(String mailAddress);

	String getRoom();
	void setRoom(String room);
}
