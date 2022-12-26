package classes;

import classes.event.Event;
import classes.event.TimeUnit;
import helper.ArrayHelper;
import helper.EventHelperService;

import java.time.LocalDateTime;

public class Location {
    private String name;
    private int maxCapacity;
    private Event[] events;

    public Location(String name, int maxCapacity){
        this.name = name;
        this.maxCapacity = maxCapacity;
    }


    public Event[] eventsWhileDuration(LocalDateTime date, double length, TimeUnit timeUnit){
        LocalDateTime endDate = switch (timeUnit) {
            case HOUR -> date.plusHours((long) Math.ceil(length));
            case DAY -> date.plusDays((long) Math.ceil(length));
            case MONTH -> date.plusMonths((long) Math.ceil(length));
        };
        Event[] eventsWhileDuration = new Event[events.length];
        for (int i = 0; i < events.length; i++) {
            if (!(events[i].getStart().isBefore(date) && EventHelperService.getEndOfEvent(events[i]).isBefore(endDate)) ||
                    (events[i].getStart().isAfter(date) && EventHelperService.getEndOfEvent(events[i]).isAfter(endDate))) {
                eventsWhileDuration[i] = events[i];
            }
        }
        return eventsWhileDuration;
    }

    public void addEvent(Event event){
        ArrayHelper.add(this.events, event);
    }

    public Event[] getEvents(){
       return this.events;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
