package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.get(0));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());

        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0));
        assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1));

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> at13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, at13.size());
        assertEquals(singleTrainingSession, at13.get(0));

        List<TrainingSession> at14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(at14.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeMultipleGroupsSameTime() {
        Timetable timetable = new Timetable();

        Coach coachOne = new Coach("Измайлов", "Кирилл", "Валерьевич");
        Coach coachTwo = new Coach("Семёнов", "Виктор", "Константинович");

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Йога для взрослых", Age.ADULT, 60);

        TrainingSession childSession = new TrainingSession(groupChild, coachOne,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession adultSession = new TrainingSession(groupAdult, coachTwo,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(childSession);
        timetable.addNewTrainingSession(adultSession);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        assertEquals(2, sessions.size());
        assertTrue(sessions.contains(childSession));
        assertTrue(sessions.contains(adultSession));
    }

    @Test
    void testGetTrainingSessionsForDayOrderIndependentOfInsertionOrder() {

        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession eveningSession = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(19, 0));
        TrainingSession morningSession = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(9, 0));
        TrainingSession noonSession = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 30));

        timetable.addNewTrainingSession(eveningSession);
        timetable.addNewTrainingSession(morningSession);
        timetable.addNewTrainingSession(noonSession);

        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertEquals(3, tuesdaySessions.size());
        assertEquals(morningSession, tuesdaySessions.get(0));
        assertEquals(noonSession, tuesdaySessions.get(1));
        assertEquals(eveningSession, tuesdaySessions.get(2));
    }

    @Test
    void testGetTrainingSessionsForEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.SUNDAY);

        assertTrue(sessions.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();

        assertEquals(1, counters.size());
        assertEquals(coach, counters.get(0).getCoach());
        assertEquals(3, counters.get(0).getCount());
    }

    @Test
    void testGetCountByCoachesOrderedDescending() {
        Timetable timetable = new Timetable();

        Coach coachIzmailov = new Coach("Измайлов", "Кирилл", "Валерьевич");
        Coach coachSemenov = new Coach("Семёнов", "Виктор", "Константинович");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coachIzmailov,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachIzmailov,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachIzmailov,
                DayOfWeek.SATURDAY, new TimeOfDay(18, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coachSemenov,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coachSemenov,
                DayOfWeek.SUNDAY, new TimeOfDay(18, 0)));

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();

        assertEquals(2, counters.size());
        assertEquals(coachIzmailov, counters.get(0).getCoach());
        assertEquals(3, counters.get(0).getCount());
        assertEquals(coachSemenov, counters.get(1).getCoach());
        assertEquals(2, counters.get(1).getCount());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> counters = timetable.getCountByCoaches();

        assertTrue(counters.isEmpty());
    }
}