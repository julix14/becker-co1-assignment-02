package classes;

import classes.event.Event;
import classes.event.TimeUnit;
import helper.ArrayHelper;
import helper.EventHelperService;

import java.time.LocalDateTime;

public class Location {
    private String name;
    private int maxCapacity;
    private Event[] events = new Event[0];

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
        Event[] eventsWhileDuration = new Event[0];

        for (Event event : this.events) {
            if (!(event.getStart().isBefore(date) && EventHelperService.getEndOfEvent(event).isBefore(endDate)) ||
                    (event.getStart().isAfter(date) && EventHelperService.getEndOfEvent(event).isAfter(endDate))) {
                ArrayHelper.add(eventsWhileDuration, event);
            }
        }
        return eventsWhileDuration;
    }

    public void addEvent(Event event){
        events = ArrayHelper.add(this.events, event);
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
