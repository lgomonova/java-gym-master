package ru.yandex.practicum.gym;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable
            = new EnumMap<>(DayOfWeek.class);

    private final Map<DayOfWeek, List<TrainingSession>> sortedSessionsByDay
            = new EnumMap<>(DayOfWeek.class);

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();

        TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable =
                timetable.computeIfAbsent(day, d -> new TreeMap<>());

        List<TrainingSession> sessionsAtTime =
                dayTimetable.computeIfAbsent(trainingSession.getTimeOfDay(), t -> new ArrayList<>());

        sessionsAtTime.add(trainingSession);

        List<TrainingSession> flattened = new ArrayList<>();
        for (List<TrainingSession> sessions : dayTimetable.values()) {
            flattened.addAll(sessions);
        }
        sortedSessionsByDay.put(day, flattened);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> sessions = sortedSessionsByDay.get(dayOfWeek);
        return sessions == null ? Collections.emptyList() : Collections.unmodifiableList(sessions);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable = timetable.get(dayOfWeek);
        if (dayTimetable == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = dayTimetable.get(timeOfDay);
        return sessions == null ? Collections.emptyList() : Collections.unmodifiableList(sessions);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> countByCoach = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayTimetable : timetable.values()) {
            for (List<TrainingSession> sessions : dayTimetable.values()) {
                for (TrainingSession session : sessions) {
                    countByCoach.merge(session.getCoach(), 1, Integer::sum);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : countByCoach.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(Comparator.comparingInt(CounterOfTrainings::getCount).reversed());

        return result;
    }
}