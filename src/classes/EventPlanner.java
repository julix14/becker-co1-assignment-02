package classes;

import classes.event.Event;
import classes.event.OnlineEvent;
import classes.event.OnsiteEvent;
import classes.event.Unit;
import classes.helper.ArrayHelper;
import classes.helper.UserInputService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

public class EventPlanner {
    private Location[] locations;

    private int eventCounter;

    private Event[] events = new Event[0];
    private final Location ONLINE_LOCATION;

    // Primary constructor
    public EventPlanner(int locationCount) {
        eventCounter = 0;
        ONLINE_LOCATION = new Location("Online", -1);

        locations = setupLocations(locationCount + 1);
    }

    //Second constructor for testing purposes
    public EventPlanner() {
        eventCounter = 0;
        ONLINE_LOCATION = new Location("Online", -1);
        locations = demoLocations();
    }

    // Setup locations for the event planner
    private Location[] setupLocations(int locationCount) {
        System.out.printf("%nPlease enter the name and max capacity of %d locations:", locationCount - 1);
        this.locations = new Location[locationCount];
        for (int i = 0; i < locationCount - 1; i++) {
            String name = UserInputService.getStringFromUserWithMessage(String.format("%nPlease enter the name of location %d: ", i + 1));
            int maxCapacity = UserInputService.getValidIntFromUser(String.format("Please enter the max. capacity of %s: ", name));
            locations[i] = new Location(name, maxCapacity);
        }
        locations[locationCount - 1] = ONLINE_LOCATION;
        return locations;
    }

    // Setup demo locations for the event planner
    // Just for testing purposes
    private Location[] demoLocations() {
        Location[] locations = new Location[3];
        locations[0] = ONLINE_LOCATION;
        locations[1] = new Location("Room 1", 10);
        locations[2] = new Location("Room 2", 20);

        events = ArrayHelper.add(events, new OnlineEvent(1, "Online Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[0]));
        events = ArrayHelper.add(events, new OnlineEvent(2, "Online Event 2", LocalDateTime.of(2021, 2, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[0]));

        events = ArrayHelper.add(events, new OnsiteEvent(3, "Onsite Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[1]));

        events = ArrayHelper.add(events, new OnsiteEvent(4, "Onsite Event 2", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.HOUR, new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(5, "Onsite Event 3", LocalDateTime.of(2021, 2, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(6, "Onsite Event 4", LocalDateTime.of(2021, 3, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[2]));
        eventCounter = 7;
        return locations;
    }

    // #### Functions selectable in the menu ####

    public void createNewEvent() {
        System.out.println("Create new event");
        // Get name of new Event
        String name = UserInputService.getStringFromUserWithMessage("Please enter the name of the event: ");

        // Get Start of new Event
        LocalDateTime startDate = UserInputService.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");

        // Get length of new Event
        int length = UserInputService.getValidIntFromUser("Please enter the length of the event: ");

        // Get TimeUnit of new Event
        int timeUnitPlace = UserInputService.getValidIntInRangeFromUser("Please enter the time unit of the event: (1 = Hours, 2 = Days, 3 = Months)", 1, 3);
        Unit unit = Unit.values()[timeUnitPlace - 1];

        // Select Location of new Event
        boolean locationAvailable;
        Location location;
        Location[] locationsToSelect = locations;

        // Check if location is free
        // If not, check if other locations are free
        // If this is not the case too, let the user select new start date
        do {
            System.out.println("Please select the location of the event:");
            location = locationSelector(locationsToSelect);

            // Check if location is online
            if (location == ONLINE_LOCATION) {
                locationAvailable = true;
            } else {
                // If Location is not online, check if it is free
                Event[] eventsOnLocation = getEventsForOneLocation(events, location);
                locationAvailable = eventsWhileDuration(eventsOnLocation, startDate, length, unit).length == 0;
            }
            // If location is not free, check if other locations are free
            if (!locationAvailable) {
                locationsToSelect = getFreeLocationsOnDate(startDate, length, unit);

                // If other locations are free, let the user select a new location
                if (ArrayHelper.remove(locationsToSelect, ONLINE_LOCATION).length != 0) {
                    System.out.println("The selected location is not available on the selected date. Please select another location:");
                } else {
                    // If no other locations are free, let the user select a new date
                    System.out.println("There are no free locations on the selected date. Please select another date:");
                    startDate = UserInputService.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");
                    locationsToSelect = locations;
                }
            }
            // Repeat until an available location is selected
        } while (!locationAvailable);

        // Get number of participants
        int participantsCount;
        if (location == ONLINE_LOCATION) {
            // If location is online, let the user select the number of participants, because there is no limit on online
            participantsCount = UserInputService.getValidIntFromUser("Please enter the number of participants: ");
        } else {
            // If location is not online, let the user select the number of participants with a max. of the max. capacity of the location
            participantsCount = UserInputService.getValidIntInRangeFromUser("Please enter the number of participants: (max. " + location.getMaxCapacity() + ")", 1, location.getMaxCapacity());
        }

        // Get names of participants
        String[] participants = new String[participantsCount];
        for (int i = 0; i < participantsCount; i++) {
            participants[i] = UserInputService.getStringFromUserWithMessage(String.format("Please enter the name of participant %d: ", i + 1));
        }

        // Create new Event
        Event event;
        if (location == ONLINE_LOCATION) {
            event = new OnlineEvent(eventCounter, name, startDate, length, unit, participants, location);

        } else {
            event = new OnsiteEvent(eventCounter, name, startDate, length, unit, participants, location);
        }

        // Increase eventCounter and add event to Event array
        events = ArrayHelper.add(events, event);
        eventCounter++;

        System.out.println("Event created successfully!");
    }

    public void showAllEvents() {
        // Print the whole Event array
        printEvents(events);
        System.out.println();
    }

    public void showEventsByTitle() {
        // Get the part of the title to search for
        String searchedPhrase = UserInputService.getStringFromUserWithMessage("Please enter the name of the searched event: ");

        Event[] selectedEvents = new Event[0];
        // Check for each event if the title contains the searched phrase
        for (Event event : events) {
            if (event.getTitle().toLowerCase().contains(searchedPhrase.toLowerCase())) {
                selectedEvents = ArrayHelper.add(selectedEvents, event);
            }
        }
        // Print the selected events
        printEvents(selectedEvents);
    }

    public void showEventsByLocation() {
        // Let the user select a location
        Location location = locationSelector(locations);

        // Get all events on the selected location and print them
        Event[] eventsOnLocation = getEventsForOneLocation(events, location);
        printEvents(eventsOnLocation);
    }

    public void showEventsByParticularDate() {
        // Get the date to search for
        LocalDate searchedDate = UserInputService.getValidLocalDateFromUser("Please enter the date of the searched events: (DD.MM.YYYY)");

        // Get all events on the selected day and print them
        Event[] allEvents = eventsWhileDuration(events, searchedDate.atStartOfDay(), 1, Unit.DAY);
        printEvents(allEvents);
    }

    public void showMostUsedLocation(){
        Location mostUsedLocation = locations[0];
        int mostUsedLocationCount = 0;

        // Check for each location how often it is used
        // If the location is used more often than the current most used location, set the current location as the new most used location
        for (Location location : locations) {
            if (getEventsForOneLocation(events, location).length > mostUsedLocationCount) {
                mostUsedLocation = location;
                mostUsedLocationCount = getEventsForOneLocation(events, location).length;
            }
        }
        System.out.printf("%nThe most used location is \"%s\" with %d events.%n%n", mostUsedLocation.getName(), mostUsedLocationCount);
    }

    public void showLeastUsedLocation() {
        Location leastUsedLocation = locations[0];
        int leastUsedLocationCount = Integer.MAX_VALUE;

        // Check for each location how often it is used
        // If the location is used less often than the current least used location, set the current location as the new least used location
        for (Location location : locations) {
            if (getEventsForOneLocation(events, location).length < leastUsedLocationCount) {
                leastUsedLocation = location;
                leastUsedLocationCount = getEventsForOneLocation(events, location).length;
            }
        }
        System.out.printf("%nThe least used location is \"%s\" with %d events.%n%n", leastUsedLocation.getName(), leastUsedLocationCount);
    }

    //#### Helper Methods ####
    private void printEvents(Event[] eventsToPrint) {
        // Set formatting constants for printing
        final String WHITE_UNDERLINED = "\033[4m";
        final String RESET = "\033[0m";

        // Sort events by ID
        Arrays.sort(eventsToPrint, Comparator.comparingInt(Event::getId));

        // Check if there are any events to print
        if (eventsToPrint.length == 0) {
            System.out.println("No events found.");
        } else {
            // Print the total count of events
            System.out.printf("%d Events found:%n", eventsToPrint.length);

            //Print events as table
            System.out.printf(WHITE_UNDERLINED + "%-4s    %-16s    %-16s    %-14s    %-14s    %3s" + RESET, "ID", "Title", "Location", "Start", "End", "ParticipantsCount");
            for (Event event : eventsToPrint) {
                System.out.printf("%n%s ", event.getInformationString());
            }
            System.out.println("\n");
        }

    }

    private Location locationSelector(Location[] locations){
        // Print all locations from the Location array
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] == ONLINE_LOCATION) {
                System.out.printf("%d. %s%n", i + 1, locations[i].getName());
            } else {
                System.out.printf("%d. %s (max. %d participants)%n", i + 1, locations[i].getName(), locations[i].getMaxCapacity());
            }
        }
        int locationPlace = UserInputService.getValidIntInRangeFromUser("Please enter the number of the location: ", 1, locations.length);
        return locations[locationPlace-1];
    }


    private LocalDateTime calculateEndOfEvent(LocalDateTime start, Unit unit, int length) {
        // Calculate the end of the event based on the start date, the unit and the length
        return switch (unit) {
            case HOUR -> start.plusHours(length);
            case DAY -> start.plusDays(length);
            case MONTH -> start.plusMonths(length);
        };
    }

    private Event[] eventsWhileDuration(Event[] events, LocalDateTime newEventStart, int length, Unit unit) {
        LocalDateTime newEventEnd = calculateEndOfEvent(newEventStart, unit, length);
        Event[] eventsWhileDuration = new Event[0];
        // Go through all events and check if the event is during the calculated duration
        for (Event event : events) {
            if (
                // Check if the current event starts before the new event starts && the current event ends after the new event starts
                    event.getStart().isBefore(newEventStart) && event.getEndOfEvent().isAfter(newEventStart) ||
                            // Check if the current event starts before the new event ends && the current event ends after the new event ends
                            event.getStart().isBefore(newEventEnd) && event.getEndOfEvent().isAfter(newEventEnd) ||
                            // Check if the new event starts before the current event starts && the new event end after the current event start
                            newEventStart.isBefore(event.getStart()) && newEventEnd.isAfter(event.getEndOfEvent()) ||
                            // Check if the current event starts before the new event starts && the current event ends after the new event ends
                            event.getStart().isBefore(newEventStart) && event.getEndOfEvent().isAfter(newEventEnd) ||
                            // Check if the new event starts at the same time as the new one or ends at the same time as the new one
                            event.getStart().isEqual(newEventStart) || event.getEndOfEvent().isEqual(newEventEnd)
            ) {
                eventsWhileDuration = ArrayHelper.add(eventsWhileDuration, event);
            }
        }
        return eventsWhileDuration;
    }

    private Location[] getFreeLocationsOnDate(LocalDateTime startDate, int length, Unit unit) {
        Location[] freeLocations = new Location[0];
        // Go through all locations
        // Get all events on the location
        // Check if any event is on the calculated duration
        for (Location location : locations) {
            Event[] eventsOnLocation = getEventsForOneLocation(events, location);
            boolean locationAvailable = eventsWhileDuration(eventsOnLocation, startDate, length, unit).length == 0;
            if (locationAvailable) {
                freeLocations = ArrayHelper.add(freeLocations, location);
            }
        }
        return freeLocations;
    }

    private Event[] getEventsForOneLocation(Event[] events, Location location) {
        Event[] eventsOnLocation = new Event[0];
        // Go through all events and check if the event is on the selected location
        // If the location is the ONLINE_LOCATION, add all Events from class "OnlineEvent"
        if (location == ONLINE_LOCATION) {
            for (Event event : events) {
                if (event.getClass() == OnlineEvent.class) {
                    eventsOnLocation = ArrayHelper.add(eventsOnLocation, event);
                }
            }
            // If the location is not the ONLINE_LOCATION
            // Go through all events and check if the event is on the selected location
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
