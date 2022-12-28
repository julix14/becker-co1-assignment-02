package classes;

import classes.event.Event;
import classes.event.OnlineEvent;
import classes.event.OnsiteEvent;
import classes.event.Unit;
import helper.ArrayHelper;
import helper.input.UserInputService;
import helper.validation.ValidationService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventPlanner {
    private Location[] locations;
    private final UserInputService USER_INPUT_SERVICE;
    private final ValidationService VALIDATION_SERVICE;

    private int eventCounter;

    private Event[] events = new Event[0];
    private final Location ONLINE_LOCATION;

    public EventPlanner(int locationCount) {
        USER_INPUT_SERVICE = new UserInputService();
        VALIDATION_SERVICE = new ValidationService();
        eventCounter = 0;
        ONLINE_LOCATION = new Location("Online", -1);

        //locations = setupLocations(locationCount + 1);
        locations = demoLocations();
    }

    private Location[] setupLocations(int locationCount){
        System.out.printf("%nPlease enter the name and max capacity of %d locations:", locationCount - 1);
        this.locations = new Location[locationCount];
        for (int i = 0; i < locationCount - 1; i++) {
            String name = USER_INPUT_SERVICE.getStringFromUserWithMessage(String.format("%nPlease enter the name of location %d: ", i + 1));
            int maxCapacity = VALIDATION_SERVICE.getValidIntFromUser(String.format("Please enter the max. capacity of %s: ", name));
            locations[i] = new Location(name, maxCapacity);
        }
        locations[locationCount - 1] = ONLINE_LOCATION;
        return locations;
    }

    private Location[] demoLocations() {
        Location[] locations = new Location[3];
        locations[0] = new Location("Online", -1);
        locations[1] = new Location("Room 1", 10);
        locations[2] = new Location("Room 2", 20);

        events = ArrayHelper.add(events, new OnlineEvent(1, "Online Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[0]));
        events = ArrayHelper.add(events, new OnlineEvent(2, "Online Event 2", LocalDateTime.of(2021, 2, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[0]));

        events = ArrayHelper.add(events, new OnsiteEvent(3, "Onsite Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[1]));

        events = ArrayHelper.add(events, new OnsiteEvent(5, "Onsite Event 3", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.HOUR, new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(6, "Onsite Event 4", LocalDateTime.of(2021, 2, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(7, "Onsite Event 5", LocalDateTime.of(2021, 3, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[2]));

        return locations;
    }

    public void createNewEvent(){
        System.out.println("Create new event");
        // Get name of new Event
        String name = USER_INPUT_SERVICE.getStringFromUserWithMessage("Please enter the name of the event: ");

        // Get Start of new Event
        LocalDateTime startDate = VALIDATION_SERVICE.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");

        // Get length of new Event
        int length = VALIDATION_SERVICE.getValidIntFromUser("Please enter the length of the event: ");

        // Get TimeUnit of new Event
        int timeUnitPlace = VALIDATION_SERVICE.getValidIntInRangeFromUser("Please enter the time unit of the event: (1 = Hours, 2 = Days, 3 = Months)", 1, 3);
        Unit unit = Unit.values()[timeUnitPlace - 1];

        // Select Location of new Event
        boolean locationAvailable;
        Location location;
        Location[] locationsToSelect = locations;

        do {
            System.out.println("Please select the location of the event:");
            location = locationSelector(ArrayHelper.add(locationsToSelect, ONLINE_LOCATION));
            if (location == ONLINE_LOCATION) {
                locationAvailable = true;
            } else {
                Event[] eventsOnLocation = eventsOnLocation(events, location);
                locationAvailable = eventsWhileDuration(eventsOnLocation, startDate, length, unit).length == 0;
            }
            if (!locationAvailable) {
                locationsToSelect = getFreeLocationsOnDate(startDate, length, unit);
                if (locationsToSelect.length != 0) {
                    System.out.println("The selected location is not available on the selected date. Please select another location:");
                } else {
                    System.out.println("There are no free locations on the selected date. Please select another date:");
                    startDate = VALIDATION_SERVICE.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");
                }
            }
        } while (!locationAvailable);

        // Add participants
        int participantsCount;
        if (location == ONLINE_LOCATION) {
            participantsCount = VALIDATION_SERVICE.getValidIntFromUser("Please enter the number of participants: ");
        } else {
            participantsCount = VALIDATION_SERVICE.getValidIntInRangeFromUser("Please enter the number of participants: ", 1, location.getMaxCapacity());
        }

        String[] participants = new String[participantsCount];
        for (int i = 0; i < participantsCount; i++) {
            participants[i] = USER_INPUT_SERVICE.getStringFromUserWithMessage(String.format("Please enter the name of participant %d: ", i + 1));
        }

        Event event;
        if (location == ONLINE_LOCATION) {
            event = new OnlineEvent(eventCounter, name, startDate, length, unit, participants, location);

        } else {
            event = new OnsiteEvent(eventCounter, name, startDate, length, unit, participants, location);
        }
        events = ArrayHelper.add(events, event);
        eventCounter++;

        System.out.println("Event created successfully!");
    }

    public void showAllEvents() {
        printEvents(events);
        System.out.println();
    }

    public void showEventsByTitle() {
        String searchedPhrase = USER_INPUT_SERVICE.getStringFromUserWithMessage("Please enter the name of the searched event: ");

        Event[] selectedEvents = new Event[0];
        for (Event event : events) {
            if (event.getTitle().toLowerCase().contains(searchedPhrase.toLowerCase())) {
                selectedEvents = ArrayHelper.add(selectedEvents, event);
            }
        }
        printEvents(selectedEvents);
    }

    public void showEventsByLocation() {
        Location location = locationSelector(locations);
        printEvents(eventsOnLocation(events, location));
    }

    public void showEventsByParticularDate() {
        LocalDate searchedDate = VALIDATION_SERVICE.getValidLocalDateFromUser("Please enter the date of the searched events: (DD.MM.YYYY)");
        Event[] allEvents = new Event[0];
        for (Location location : locations) {
            allEvents = ArrayHelper.addAll(
                    eventsWhileDuration(
                            eventsOnLocation(events, location),
                            searchedDate.atStartOfDay(),
                            1,
                            Unit.DAY),
                    allEvents);
        }
        printEvents(allEvents);
    }

    public void showMostUsedLocation(){
        Location mostUsedLocation = locations[0];
        int mostUsedLocationCount = 0;
        for (Location location : locations) {
            if (eventsOnLocation(events, location).length > mostUsedLocationCount) {
                mostUsedLocation = location;
                mostUsedLocationCount = eventsOnLocation(events, location).length;
            }
        }
        System.out.printf("%nThe most used location is %s with %d events.%n", mostUsedLocation.getName(), mostUsedLocationCount);
    }

    public void showLeastUsedLocation(){
        Location leastUsedLocation = locations[0];
        int leastUsedLocationCount = Integer.MAX_VALUE;
        for (Location location : locations) {
            if (eventsOnLocation(events, location).length < leastUsedLocationCount) {
                leastUsedLocation = location;
                leastUsedLocationCount = eventsOnLocation(events, location).length;
            }
        }
        System.out.printf("%nThe least used location is %s with %d events.%n", leastUsedLocation.getName(), leastUsedLocationCount);

    }


    private void printEvents(Event[] events) {
        // Set formatting constants for printing
        final String WHITE_UNDERLINED = "\033[4m";
        final String RESET = "\033[0m";

        // Sort events by ID
        events = ArrayHelper.sortById(events);

        //Print events as table
        if (events.length == 0) {
            System.out.println("No events found.");
        } else {
            System.out.printf("%d Events found:%n", events.length);
            System.out.printf(WHITE_UNDERLINED + "  %-4s    %-16s    %-10s    %-14s    %-14s" + RESET, "ID", "Title", "Location", "Start", "End");
            for (Event event : events) {
                System.out.printf("%n  %s ", event.getInformation());
            }
            System.out.println("\n");
        }

    }

    private Location locationSelector(Location[] locations){
        for (int i = 0; i < locations.length; i++) {
            System.out.printf("%d: %s%n", i+1, locations[i].getName());
        }
        int locationPlace = VALIDATION_SERVICE.getValidIntInRangeFromUser("Please enter the number of the location: ", 1, locations.length);
        return locations[locationPlace-1];
    }

    private Location[] getFreeLocationsOnDate(LocalDateTime startDate, int length, Unit unit) {
        Location[] freeLocations = new Location[0];
        for (Location location : locations) {
            Event[] eventsOnLocation = eventsOnLocation(events, location);
            boolean locationAvailable = eventsWhileDuration(eventsOnLocation, startDate, length, unit).length == 0;
            if (locationAvailable && location != ONLINE_LOCATION) {
                freeLocations = ArrayHelper.add(freeLocations, location);
            }
        }
        return freeLocations;
    }


    private LocalDateTime getEndOfEvent(LocalDateTime start, Unit unit, int length) {
        return switch (unit) {
            case HOUR -> start.plusHours(length);
            case DAY -> start.plusDays(length);
            case MONTH -> start.plusMonths(length);
        };
    }

    private Event[] eventsWhileDuration(Event[] events, LocalDateTime date, int length, Unit unit) {
        LocalDateTime endDate = getEndOfEvent(date, unit, length);
        Event[] eventsWhileDuration = new Event[0];

        for (Event event : events) {
            if (!(event.getEndOfEvent().isBefore(date) || (event.getStart().isAfter(endDate)))) {
                eventsWhileDuration = ArrayHelper.add(eventsWhileDuration, event);
            }
        }
        return eventsWhileDuration;
    }

    private Event[] eventsOnLocation(Event[] events, Location location) {
        Event[] eventsOnLocation = new Event[0];
        if (location == ONLINE_LOCATION) {
            for (Event event : events) {
                if (event.getClass() == OnlineEvent.class) {
                    eventsOnLocation = ArrayHelper.add(eventsOnLocation, event);
                }
            }
        } else {
            for (Event event : events) {
                if (event.getClass() == OnsiteEvent.class && ((OnsiteEvent) event).getLocation() == location) {
                    eventsOnLocation = ArrayHelper.add(eventsOnLocation, event);
                }
            }
        }
        return eventsOnLocation;
    }

}
