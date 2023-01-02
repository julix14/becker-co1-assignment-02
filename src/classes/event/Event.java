package classes.event;

import java.time.LocalDateTime;

public abstract class Event {
    protected final int ID;
    protected String title;
    protected LocalDateTime start;
    protected double length;
    protected Unit unit;
    protected String[] participants;

    public Event(int ID, String title, LocalDateTime start, double length, Unit unit, String[] participants) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.length = length;
        this.unit = unit;
        this.participants = participants;
    }

    public int getId() {
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

    public Unit getUnit() {
        return unit;
    }

    public abstract String getInformationString();
}
