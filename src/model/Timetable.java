package model;

import model.TimeOfDay;
import model.TrainingSession;
import service.DayOfWeek;

import java.util.*;
import java.util.Comparator;
import java.util.List;

public class Timetable {

    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> dayAndTimeToSession;


    public Timetable () {
        dayAndTimeToSession = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek days : DayOfWeek.values()) {
            dayAndTimeToSession.put(days, new TreeMap<>(Comparator.naturalOrder()));
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {

        TimeOfDay time = trainingSession.getTimeOfDay();
        DayOfWeek day = trainingSession.getDayOfWeek();

        dayAndTimeToSession.get(day)
                .computeIfAbsent(time, t -> new ArrayList<>())
                .add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> sessions : dayAndTimeToSession.get(dayOfWeek).values()) {
            result.addAll(sessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> time = dayAndTimeToSession.get(dayOfWeek);
        List<TrainingSession> session = time.get(timeOfDay);

        if (session == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(session);
    }

    public List<CounterOfTrainings> getCountByCoaches () {
        Map<Coach, Integer> coachCount = new HashMap<>();

        for (Map<TimeOfDay, List<TrainingSession>> timeSessions : dayAndTimeToSession.values()) {
            for (List<TrainingSession> sessions : timeSessions.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCount.compute(coach, (c, count) -> count == null ? 1 : count + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();

        for(Map.Entry<Coach, Integer> entry : coachCount.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(result);

        return result;
    }

}
