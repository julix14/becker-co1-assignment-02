package helper;

import classes.event.Event;
import classes.event.TimeUnit;

import java.time.LocalDateTime;

public class EventHelperService {
    public static LocalDateTime getEndOfEvent(Event event) {
        TimeUnit timeUnit = event.getTimeUnit();
        double length = event.getLength();
        LocalDateTime start = event.getStart();

        return switch (timeUnit) {
            case HOUR -> start.plusHours((long) Math.ceil(length));
            case DAY -> start.plusDays((long) Math.ceil(length));
            case MONTH -> start.plusMonths((long) Math.ceil(length));
        };
    }
}
