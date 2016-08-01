package net.sharksystem.sharknet.api;

import java.util.*;

/**
 * Created by Thilo S. on 15.07.2016.
 */
public class ImplSchoolMetadata implements SchoolMetadata {
	private ArrayList<String> lessonNames = new ArrayList<>();
	private ArrayList<String> teacherNames = new ArrayList<>();
	private ArrayList<String> rooms = new ArrayList<>();
	private int lessonDurationInMinutes = 0;
	private HashMap<Integer, Integer> breaks = null;
	private List<Date> dayOff = new ArrayList<>();
	private int summerOrWinterSemester;
	private Date startSummerSemester = null;
	private Date endSummerSemester = null;
	private Date startWinterSemester = null;
	private Date endWinterSemester = null;
	private Date startEarliestLesson = null;

	ImplSchoolMetadata(ArrayList<String> lessonNames, ArrayList<String> teacherNames, ArrayList<String> rooms,
					   int lessonDurationInMinutes, HashMap<Integer, Integer> breaks, List<Date> dayOff, int summerOrWinterSemester,
					   Date startSummerSemester, Date endSummerSemester, Date startWinterSemester, Date endWinterSemester, Date startEarliestLesson) {
		this.lessonNames = lessonNames;
		this.teacherNames = teacherNames;
		this.rooms = rooms;
		this.lessonDurationInMinutes = lessonDurationInMinutes;
		this.breaks = breaks;
		this.dayOff = dayOff;
		this.setSummerOrWinterSemester(summerOrWinterSemester);
		this.startSummerSemester = startSummerSemester;
		this.endSummerSemester = endSummerSemester;
		this.startWinterSemester = startWinterSemester;
		this.endWinterSemester = endWinterSemester;
		this.startEarliestLesson = startEarliestLesson;
	}

	@Override
	public ArrayList<String> getLessonNames() {
		return lessonNames;
	}

	@Override
	public void setLessonNames(ArrayList<String> lessonNames) {
		this.lessonNames = lessonNames;
	}

	@Override
	public ArrayList<String> getTeacherNames() {
		return teacherNames;
	}

	@Override
	public void setTeacherNames(ArrayList<String> teacherNames) {
		this.teacherNames = teacherNames;
	}

	@Override
	public ArrayList<String> getRooms() {
		return rooms;
	}

	@Override
	public void setRooms(ArrayList<String> rooms) {
		this.rooms = rooms;
	}

	@Override
	public int getLessonDurationInMinutes() {
		return lessonDurationInMinutes;
	}

	@Override
	public void setLessonDurationInMinutes(int lessonDurationInMinutes) {
		this.lessonDurationInMinutes = lessonDurationInMinutes;
	}

	@Override
	public HashMap<Integer, Integer> getBreaks() {
		return breaks;
	}

	@Override
	public void setBreaks(HashMap<Integer, Integer> breaks) {
		this.breaks = breaks;
	}

	@Override
	public List<Date> getDayOff() {
		return dayOff;
	}

	@Override
	public void setDayOff(List<Date> dayOff) {
		this.dayOff = dayOff;
	}

	@Override
	public int getSummerOrWinterSemester() {
		if (summerOrWinterSemester == 0 || summerOrWinterSemester == 1) {
			return summerOrWinterSemester;
		} else {
			throw new IllegalArgumentException("Wrong Argument, SummerOrWinterSemester can be 0 for winter or 1 for summer!");
		}
	}

	@Override
	public void setSummerOrWinterSemester(int summerOrWinterSemester) {
		this.summerOrWinterSemester = summerOrWinterSemester;
	}

	@Override
	public Date getStartSummerSemester() {
		return startSummerSemester;
	}

	@Override
	public void setStartSummerSemester(Date startSummerSemester) {
		this.startSummerSemester = startSummerSemester;
	}

	@Override
	public Date getEndSummerSemester() {
		return endSummerSemester;
	}

	@Override
	public void setEndSummerSemester(Date endSummerSemester) {
		this.endSummerSemester = endSummerSemester;
	}

	@Override
	public int calculateLengthOfSummerSemester() {
		return safeLongToInt(calculateLengthOfSemester(startSummerSemester, endSummerSemester,
			new IllegalArgumentException("Start/End of SummerSemester not set yet!")));
	}

	@Override
	public Date getStartWinterSemester() {
		return startWinterSemester;
	}

	@Override
	public void setStartWinterSemester(Date startWinterSemester) {
		this.startWinterSemester = startWinterSemester;
	}

	@Override
	public Date getEndWinterSemester() {
		return endWinterSemester;
	}

	@Override
	public void setEndWinterSemester(Date endWinterSemester) {
		this.endWinterSemester = endWinterSemester;
	}

	@Override
	public int calculateLengthOfWinterSemester() {
		return safeLongToInt(calculateLengthOfSemester(startWinterSemester, endWinterSemester,
			new IllegalArgumentException("Start/End of WinterSemester not set yet!")));
	}

	@Override
	public Date getStartEarliestLesson() {
		return startEarliestLesson;
	}

	@Override
	public void setStartEarliestLesson(Date startEarliestLesson) {
		this.startEarliestLesson = startEarliestLesson;
	}

	public int calculateLengthOfSemester(Date startDate, Date endDate, IllegalArgumentException e) {
		if (startDate != null && endDate != null) {
			long different = endDate.getTime() - startDate.getTime();

			long secondsInMilli = 1000;
			long minutesInMilli = secondsInMilli * 60;
			long hoursInMilli = minutesInMilli * 60;
			long daysInMilli = hoursInMilli * 24;

			return safeLongToInt(different / daysInMilli);
		} else {
			throw e;
		}
	}

	public HashMap<Integer, Date> getIndexMatrixOfDaysWithoutWeekdays(Date startDate, Date endDate) {
		int days = calculateLengthOfSemester(startDate, endDate, new IllegalArgumentException("StartDate or EndDate are not initialised!"));
		HashMap<Integer, Date> resultList = new HashMap<>();
		int index = 0;
		for (int i = 0; i <= days; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.add(Calendar.DATE, i);
			if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				resultList.put(index, c.getTime());
				index += 1;
			}
		}
		return resultList;
	}

	public int searchIndexOfDayInIndexMatrix(Date searchedDate, HashMap<Integer, Date> indexMatrix) {
		int result = -1;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		for (int i = 0; i < indexMatrix.size(); i++) {
			c1.setTime(indexMatrix.get(i));
			c2.setTime(searchedDate);
			if (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
				result = i;
			}
		}
		return result;
	}

	private static int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new IllegalArgumentException
				(l + " cannot be cast to int without changing its value.");
		}
		return (int) l;
	}
}
