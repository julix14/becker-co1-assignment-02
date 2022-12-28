package classes.event;

import classes.Location;

import java.time.LocalDateTime;

public class OnsiteEvent extends Event {
    private Location location;


    public OnsiteEvent(int ID, String title, LocalDateTime start, long length, Unit unit, String[] participants, Location location) {
        super(ID, title, start, length, unit, participants);
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }
}
