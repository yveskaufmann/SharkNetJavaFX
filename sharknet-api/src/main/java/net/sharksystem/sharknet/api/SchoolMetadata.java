package net.sharksystem.sharknet.api;

import org.javatuples.Pair;

import java.util.Date;
import java.util.List;

/**
 * Created by Thilo S. on 15.07.2016.
 */
public interface SchoolMetadata {

    List<String> getLessonNames();
    void setLessonNames(List<String> lessonNames);

    List<String> getTeacherNames();
    void setTeacherNames(List<String> teacherNames);

    List<String> getRooms();
    void setRooms(List<String> rooms);

    int getLessonDurationInMinutes();
    void setLessonDurationInMinutes(int lessonDurationInMinutes);

    List<Pair<Integer, Integer>> getBreaks();
    void setBreaks(List<Pair<Integer, Integer>> breaks);

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

    List<Pair<Integer, Date>> getIndexMatrixOfDaysWithoutWeekdays(Date startDate, Date endDate);

    int searchIndexOfDayInIndexMatrix(Date searchedDate, List<Pair<Integer, Date>> indexMatrix);
}
