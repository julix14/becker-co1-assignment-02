package classes;

import classes.event.Event;
import classes.event.OnlineEvent;
import classes.event.OnsiteEvent;
import classes.event.Unit;
import classes.helper.ArrayHelper;
import classes.helper.UserInputService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class EventPlanner {
    private Location[] locations;

    private int eventCounter;

    private Event[] events;
    private final Location ONLINE_LOCATION;

    // Constructor
    public EventPlanner(int locationCount) {
        eventCounter = 0;

        ONLINE_LOCATION = new Location("Online", -1);

        events = new Event[0];

        locations = setupLocations(locationCount + 1);
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

    // #### Functions selectable in the menu ####

    public void createNewEvent() {
        System.out.println("Create new event");
        // Get name of new Event
        String name = UserInputService.getStringFromUserWithMessage("Please enter the name of the event: ");

        // Get Start of new Event
        LocalDateTime startDate = UserInputService.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");

        // Get length of new Event
        double length = UserInputService.getValidDoubleFromUser("Please enter the length of the event: ");

        // Get TimeUnit of new Event
        int timeUnitPlace = UserInputService.getValidIntInRangeFromUser("Please enter the time unit of the event: (1 = Hours, 2 = Days, 3 = Months)", 1, 3);
        Unit unit = Unit.values()[timeUnitPlace - 1];

        boolean isOnline = UserInputService.getBooleanFromYesOrNoInput("Is the event online? (y/n)");
        Location location = null;
        boolean isLocationAvailable = false;
        if (isOnline) {
            location = ONLINE_LOCATION;
            isLocationAvailable = true;
        }

        while (!isLocationAvailable) {
            // Check which locations are available
            Location[] availableLocations = getFreeLocationsOnDate(startDate, length, unit);
            availableLocations = ArrayHelper.remove(availableLocations, ONLINE_LOCATION);

            // Check if there are any locations available for the chosen date
            if (availableLocations.length != 0) {
                // Print available locations and let the user select one of it
                System.out.println("Select a location from the available locations:");
                location = locationSelector(availableLocations);
                isLocationAvailable = true;
            } else {
                // If no location available, ask the user if he wants to transform it into an online event
                System.out.println("There are no locations available on this date.");
                isOnline = UserInputService.getBooleanFromYesOrNoInput("Want to transform to online event, else you have to chose a new date? (y/n)");
                if (isOnline) {
                    location = ONLINE_LOCATION;
                    isLocationAvailable = true;
                    continue;
                }
                // If the user don't want to transform it, ask him for a new date
                System.out.println("Then you have to enter a new starting date.");
                startDate = UserInputService.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");
            }

        }


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
        final DateTimeFormatter CUSTOM_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        // Set index constants for information Array
        final int ID_INDEX = 0;
        final int TITLE_INDEX = 1;
        final int LOCATION_INDEX = 2;
        final int START_DATE_INDEX = 3;
        final int LENGTH_INDEX = 4;
        final int UNIT_INDEX = 5;
        final int PARTICIPANTS_COUNT_INDEX = 6;

        // Sort events by ID
        Arrays.sort(eventsToPrint, Comparator.comparingInt(Event::getId));

        // Check if there are any events to print
        if (eventsToPrint.length == 0) {
            System.out.println("No events found.");
        } else {
            // Print the total count of events
            System.out.printf("%d Events found:%n", eventsToPrint.length);

            //Print events as table
            System.out.printf(WHITE_UNDERLINED + "%-4s    %-16s    %-16s    %-14s    %-6s    %-5s    %-14s    %3s" + RESET, "ID", "Title", "Location", "Start-Time", "Length", "Unit", "End-Time", "Participants");
            for (Event event : eventsToPrint) {
                String[] infoArray = event.getInformationArray();
                LocalDateTime endDate = calculateEndOfEvent(event.getStart(), event.getUnit(), event.getLength());

                System.out.printf("%n[%2s]    %-16s    %-16s    %-14s    %-6s    %-5s    %-14s    %-3s", infoArray[ID_INDEX], infoArray[TITLE_INDEX], infoArray[LOCATION_INDEX], infoArray[START_DATE_INDEX], infoArray[LENGTH_INDEX], infoArray[UNIT_INDEX], endDate.format(CUSTOM_DATE_FORMAT), infoArray[PARTICIPANTS_COUNT_INDEX]);

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
        return locations[locationPlace - 1];
    }


    public LocalDateTime calculateEndOfEvent(LocalDateTime start, Unit unit, double length) {
        int HOURS_PER_DAY = 24;
        double DAYS_PER_MONTH = 30.417;


        LocalDateTime end = start;
        // Calculate the end of the event based on the start date, the unit and the length
        // Round the hours to the next biggest integer to avoid event collisions
        double afterComma = length - (int) length;
        switch (unit) {
            // If Unit equals Hours, add hours and round them to the next bigger integer
            case HOUR -> end = start.plusHours((int) Math.ceil(length));
            // If Unit equals Day, firstly add the full days (value in front of the comma)
            // After this add the partial days as hours
            case DAY -> {
                end = start.plusDays((int) length);
                end = end.plusHours((int) (afterComma * HOURS_PER_DAY));
            }
            // // If Unit equals Month, firstly add the full months (value in front of the comma)
            // After this add the partial months as days, and partial days as hours
            case MONTH -> {
                end = start.plusMonths((int) length);
                end = end.plusDays((int) (afterComma * DAYS_PER_MONTH));
                afterComma = (afterComma * DAYS_PER_MONTH) - ((int) (afterComma * DAYS_PER_MONTH));
                end = end.plusHours((int) (afterComma * HOURS_PER_DAY));
            }
        }

        return end;
    }

    private Event[] eventsWhileDuration(Event[] events, LocalDateTime newEventStart, double length, Unit unit) {
        LocalDateTime newEventEnd = calculateEndOfEvent(newEventStart, unit, length);
        Event[] eventsWhileDuration = new Event[0];

        // Go through all events and check if the event is during the calculated duration
        for (Event event : events) {
            if (
                // Check if the current event starts before the new event starts && the current event ends after the new event starts
                    event.getStart().isBefore(newEventStart) && calculateEndOfEvent(event.getStart(), event.getUnit(), event.getLength()).isAfter(newEventStart) ||
                            // Check if the current event starts before the new event ends && the current event ends after the new event ends
                            event.getStart().isBefore(newEventEnd) && calculateEndOfEvent(event.getStart(), event.getUnit(), event.getLength()).isAfter(newEventEnd) ||
                            // Check if the new event starts before the current event starts && the new event end after the current event start
                            newEventStart.isBefore(event.getStart()) && newEventEnd.isAfter(calculateEndOfEvent(event.getStart(), event.getUnit(), event.getLength())) ||
                            // Check if the current event starts before the new event starts && the current event ends after the new event ends
                            event.getStart().isBefore(newEventStart) && calculateEndOfEvent(event.getStart(), event.getUnit(), event.getLength()).isAfter(newEventEnd) ||
                            // Check if the new event starts at the same time as the new one or ends at the same time as the new one
                            event.getStart().isEqual(newEventStart) || calculateEndOfEvent(event.getStart(), event.getUnit(), event.getLength()).isEqual(newEventEnd)
            ) {
                eventsWhileDuration = ArrayHelper.add(eventsWhileDuration, event);
            }
        }
        return eventsWhileDuration;
    }

    private Location[] getFreeLocationsOnDate(LocalDateTime startDate, double length, Unit unit) {
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
