package net.sharksystem.sharknet.api;




import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thilo S. on 15.07.2016.
 */
public interface SchoolMetadata {

	ArrayList<String> getLessonNames();
	void setLessonNames(ArrayList<String> lessonNames);

	ArrayList<String> getTeacherNames();
	void setTeacherNames(ArrayList<String> teacherNames);

	ArrayList<String> getRooms();
	void setRooms(ArrayList<String> rooms);

	int getLessonDurationInMinutes();
	void setLessonDurationInMinutes(int lessonDurationInMinutes);

	HashMap<Integer, Integer> getBreaks();
	void setBreaks(HashMap<Integer, Integer> breaks);

	List<Date> getDayOff();
	void setDayOff(List<Date> dayOff);

	//0 == winter | 1 == summer
	int getSummerOrWinterSemester();
	void setSummerOrWinterSemester(int summerOrWinterSemester);

	Date getStartSummerSemester();
	void setStartSummerSemester(Date startSummerSemester);

	Date getEndSummerSemester();
	void setEndSummerSemester(Date endSummerSemester);

	int calculateLengthOfSummerSemester();

	Date getStartWinterSemester();
	void setStartWinterSemester(Date startWinterSemester);

	Date getEndWinterSemester();
	void setEndWinterSemester(Date endWinterSemester);

	int calculateLengthOfWinterSemester();

	Date getStartEarliestLesson();
	void setStartEarliestLesson(Date startEarliestLesson);

	int calculateLengthOfSemester(Date startDate, Date endDate, IllegalArgumentException e);

	HashMap<Integer, Date> getIndexMatrixOfDaysWithoutWeekdays(Date startDate, Date endDate);

	int searchIndexOfDayInIndexMatrix(Date searchedDate, HashMap<Integer, Date> indexMatrix);
}
