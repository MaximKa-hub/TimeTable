package model;

import model.TimeOfDay;
import model.TrainingSession;
import service.DayOfWeek;

import java.util.*;
import java.util.Comparator;
import java.util.List;

public class Timetable {

    private final Map<DayOfWeek, List<TrainingSession>> dayToSession;
    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> dayAndTimeToSession;


    public Timetable () {
        dayToSession = new EnumMap<>(DayOfWeek.class);
        dayAndTimeToSession = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek days : DayOfWeek.values()) {
            dayToSession.put(days, new ArrayList<>());
            dayAndTimeToSession.put(days, new HashMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {

        TimeOfDay time = trainingSession.getTimeOfDay();
        DayOfWeek day = trainingSession.getDayOfWeek();

        List<TrainingSession> daySession = dayToSession.get(day);
        daySession.add(trainingSession);
        daySession.sort(new Comparator<TrainingSession>() {
            @Override
            public int compare(TrainingSession o1, TrainingSession o2) {
                return o1.getTimeOfDay().compareTo(o2.getTimeOfDay());
            }
        });
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return new ArrayList<>(dayToSession.get(dayOfWeek));
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

        for (List<TrainingSession> daySessions : dayToSession.values()) {
            for (TrainingSession session : daySessions) {
                Coach coach = session.getCoach();
                coachCount.put(coach, coachCount.getOrDefault(coach,0) + 1);
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();

        for(Map.Entry<Coach, Integer> entry : coachCount.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings o1, CounterOfTrainings o2) {
                return o2.getCount() - o1.getCount();
            }
        });

        return result;
    }

}