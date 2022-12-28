package helper;

import classes.Location;
import classes.event.Event;
import classes.event.Unit;

import java.time.LocalDateTime;

//Ein Service für 4 Helper function ist ziemlich unnötig. Meiner Meinung nach gehört das einfach in dein EventPlanner
//Die Functions sind alle public, werden aber nur von dem EventPlanner genutzt. Damit können sie auch einfach privat im EventManger sein
public class EventHelperService {
    public static LocalDateTime getEndOfEvent(Event event) {
        Unit unit = event.getUnit();
        int length = event.getLength();
        LocalDateTime start = event.getStart();

        return switch (unit) {
            case HOUR -> start.plusHours(length);
            case DAY -> start.plusDays(length);
            case MONTH -> start.plusMonths(length);
        };
    }

    public static LocalDateTime getEndOfEvent(LocalDateTime start, Unit unit, int length) {
        return switch (unit) {
            case HOUR -> start.plusHours(length);
            case DAY -> start.plusDays(length);
            case MONTH -> start.plusMonths(length);
        };
    }

    public static Event[] eventsWhileDuration(Event[] events, LocalDateTime date, int length, Unit unit) {
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
