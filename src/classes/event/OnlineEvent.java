package classes.event;

import classes.Location;

import java.time.LocalDateTime;

public class OnlineEvent extends Event {
    //Constant name convention
    private final Location location;

    public OnlineEvent(int ID, String title, LocalDateTime start, long length, Unit unit, String[] participants, Location location) {
        super(ID, title, start, length, unit, participants);
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

}
