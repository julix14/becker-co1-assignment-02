package classes.event;

import classes.Location;

import java.time.LocalDateTime;

public class OnsiteEvent extends Event {
    private Location location;


    public OnsiteEvent(int ID, String title, LocalDateTime start, double length, TimeUnit timeUnit, String[] participants, Location location) {
        super(ID, title, start, length, timeUnit, participants);
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }
}
