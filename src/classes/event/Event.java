package classes.event;

import classes.Location;

import java.time.LocalDateTime;

public class Event {
    private final int ID;
    private String title;
    private LocalDateTime start;
    private double length;
    private Unit unit;
    private String[] participants;

    public Event(int ID, String title, LocalDateTime start, double length, Unit unit, String[] participants) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.length = length;
        this.unit = unit;
        this.participants = participants;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }


    public LocalDateTime getStart() {
        return start;
    }

    public double getLength() {
        return length;
    }


    public Unit getTimeUnit() {
        return unit;
    }

    public Location getLocation() {
        return null;
    }

}
