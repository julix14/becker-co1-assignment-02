package data.event;

import data.Location;

import java.time.LocalDateTime;

public class OfflineEvent extends Event {
    private final Location LOCATION;

    public OfflineEvent(int ID, String title, LocalDateTime start, double length, TimeUnit timeUnit, String[] participants) {
        super(ID, title, start, length, timeUnit, participants);
        this.LOCATION = new Location("Online", -1);
    }

    public Location getLocation() {
        return this.LOCATION;
    }
}
