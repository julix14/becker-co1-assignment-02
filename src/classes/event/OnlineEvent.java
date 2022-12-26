package classes.event;

import classes.Location;

import java.time.LocalDateTime;

public class OnlineEvent extends Event {
    private final Location LOCATION;

    public OnlineEvent(int ID, String title, LocalDateTime start, double length, TimeUnit timeUnit, String[] participants, Location location) {
        super(ID, title, start, length, timeUnit, participants);
        this.LOCATION = location;
    }

    public Location getLocation() {
        return this.LOCATION;
    }
}