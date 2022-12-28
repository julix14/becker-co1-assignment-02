package helper;

import classes.Location;
import classes.event.Event;
import classes.event.Unit;

import java.time.LocalDateTime;

public class EventHelperService {
    public static LocalDateTime getEndOfEvent(Event event) {
        Unit unit = event.getUnit();
        long length = event.getLength();
        LocalDateTime start = event.getStart();

        return switch (unit) {
            case HOUR -> start.plusHours(length);
            case DAY -> start.plusDays(length);
            case MONTH -> start.plusMonths(length);
        };
    }

    public static LocalDateTime getEndOfEvent(LocalDateTime start, Unit unit, long length) {
        return switch (unit) {
            case HOUR -> start.plusHours(length);
            case DAY -> start.plusDays(length);
            case MONTH -> start.plusMonths(length);
        };
    }

    public static Event[] eventsWhileDuration(Event[] events, LocalDateTime date, long length, Unit unit) {
        LocalDateTime endDate = getEndOfEvent(date, unit, length);
        Event[] eventsWhileDuration = new Event[0];

        for (Event event : events) {
            if (!(EventHelperService.getEndOfEvent(event).isBefore(date) || (event.getStart().isAfter(endDate)))) {
                eventsWhileDuration = ArrayHelper.add(eventsWhileDuration, event);
            }
        }
        return eventsWhileDuration;
    }

    public static Event[] eventsOnLocation(Event[] events, Location location){
        Event[] eventsOnLocation = new Event[0];

        for (Event event : events) {
            if (event.getLocation() == location) {
                eventsOnLocation = ArrayHelper.add(eventsOnLocation, event);
            }
        }
        return eventsOnLocation;
    }
}
