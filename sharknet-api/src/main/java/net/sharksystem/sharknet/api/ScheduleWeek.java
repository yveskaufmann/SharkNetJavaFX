package net.sharksystem.sharknet.api;

/**
 * Created by Thilo S. on 19.07.2016.
 */
public interface ScheduleWeek {

    String[] getMondayLessonNames();
    void setMondayLessonNames(String[] mondayLessonNames);

    String[] getMondayTeachers();
    void setMondayTeachers(String[] mondayTeachers);

    String[] getMondayRooms();
    void setMondayRooms(String[] mondayRooms);

    String[] getMondayPeriods();
    void setMondayPeriods(String[] mondayPeriods);

    String[] getTuesdayLessonNames();
    void setTuesdayLessonNames(String[] tuesdayLessonNames);

    String[] getTuesdayTeachers();
    void setTuesdayTeachers(String[] tuesdayTeachers);

    String[] getTuesdayRooms();
    void setTuesdayRooms(String[] tuesdayRooms);

    String[] getTuesdayPeriods();
    void setTuesdayPeriods(String[] tuesdayPeriods);

    String[] getWednesdayLessonNames();
    void setWednesdayLessonNames(String[] wednesdayLessonNames);

    String[] getWednesdayTeachers();
    void setWednesdayTeachers(String[] wednesdayTeachers);

    String[] getWednesdayRooms();
    void setWednesdayRooms(String[] wednesdayRooms);

    String[] getWednesdayPeriods();
    void setWednesdayPeriods(String[] wednesdayPeriods);

    String[] getThursdayLessonNames();
    void setThursdayLessonNames(String[] thursdayLessonNames);

    String[] getThursdayTeachers();
    void setThursdayTeachers(String[] thursdayTeachers);

    String[] getThursdayRooms();
    void setThursdayRooms(String[] thursdayRooms);

    String[] getThursdayPeriods();
    void setThursdayPeriods(String[] thursdayPeriods);

    String[] getFridayLessonNames();
    void setFridayLessonNames(String[] fridayLessonNames);

    String[] getFridayTeachers();
    void setFridayTeachers(String[] fridayTeachers);

    String[] getFridayRooms();
    void setFridayRooms(String[] fridayRooms);

    String[] getFridayPeriods();
    void setFridayPeriods(String[] fridayPeriods);
}
