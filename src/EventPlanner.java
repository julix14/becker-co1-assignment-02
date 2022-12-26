import classes.Location;
import classes.event.Event;
import classes.event.OnlineEvent;
import classes.event.OnsiteEvent;
import classes.event.TimeUnit;
import helper.ArrayHelper;
import helper.input.UserInputService;
import helper.validation.ValidationService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class EventPlanner {
    private Location[] locations;
    private final String[] FUNCTIONS = {
            "Create new event",
            "Show a report of all events",
            "Search events by title",
            "Search events by location",
            "Show all events on particular date",
            "Show the most used location",
            "Show the least used location",
    };
    UserInputService userInputService = new UserInputService();
    ValidationService validationService = new ValidationService();

    int eventCount = 0;

    public EventPlanner(int locationCount){
        locations = new Location[locationCount + 1];
    }

    public void runPlanner(){
        this.locations = setupLocations();
        System.out.println(Arrays.toString(this.locations));

        //While loop with MenuHandlerRun Call and switch case with return value
    }

    private Location[] setupLocations(){
        System.out.printf("%nPlease enter the name and max capacity of %d locations:", this.locations.length);
        for (int i = 0; i < locations.length - 1; i++) {
            String name = userInputService.getStringFromUserWithMessage(String.format("%nPlease enter the name of location %d: ", i+1));
            int maxCapacity = validationService.validateInputIsInt(String.format("Please enter the max. capacity of %s: ", name));
            locations[i] = new Location(name, maxCapacity);
        }
        locations[locations.length - 1] = new Location("Online", -1);
        return locations;
    }

    private void createNewEvent(){
        System.out.println("Create new event");
        // Get name of new Event
        String name = userInputService.getStringFromUserWithMessage("Please enter the name of the event: ");

        // Get Start of new Event
        LocalDateTime startDate = validationService.validateInputIsLocalDateTime("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");

        // Get length of new Event
        double length = validationService.validateInputIsDouble("Please enter the length of the event: ");

        // Get TimeUnit of new Event
        int timeUnitPlace = validationService.validateInputIsInRange("Please enter the time unit of the event: (1 = Hours, 2 = Days, 3 = Months)", 1, 3);
        TimeUnit timeUnit = TimeUnit.values()[timeUnitPlace-1];

        // Select Location of new Event
        System.out.println("Please select the location of the event:");
        Location location = locationSelector();

        // Check if location is available
        // Disable Check if Online Event#
        boolean locationAvailable;
        if (location.getName().equals("Online")){
            locationAvailable = true;
        } else {
            locationAvailable = location.eventsWhileDuration(startDate, length, timeUnit).length == 0;
        }

        while(!locationAvailable){
            System.out.println("The location is not available at this time. Please select another location:");
            location = locationSelector();
            locationAvailable = location.eventsWhileDuration(startDate, length, timeUnit).length == 0;
        }

        // Add participants
        int participantsCount = validationService.validateInputIsInRange("Please enter the number of participants: ", 1, location.getMaxCapacity());
        String[] participants = new String[participantsCount];
        for (int i = 0; i < participantsCount; i++) {
            participants[i] = userInputService.getStringFromUserWithMessage(String.format("Please enter the name of participant %d: ", i+1));
        }

        // Create Event and add to location
        if (location.getName().equals("Online")){
            Event event = new OnlineEvent(eventCount, name, startDate, length, timeUnit, participants, location);
            location.addEvent(event);

        } else {
            Event event = new OnsiteEvent(eventCount, name, startDate, length, timeUnit, participants, location);
            location.addEvent(event);

        }
    }

    private Event[] getAllEvents(){
        Event[] allEvents = new Event[0];
        for (Location location : locations) {
            for (Event event : location.getEvents()) {
                ArrayHelper.add(allEvents, event);
            }
        }
        return allEvents;
    }

    private Event[] getEventsByName(){
        String searchedName = userInputService.getStringFromUserWithMessage("Please enter the name of the searched event: ").toLowerCase();
        Event[] allEvents = getAllEvents();
        Event[] selectedEvents = new Event[0];
        for (Event event : allEvents) {
            if (event.getTitle().toLowerCase().contains(searchedName)){
                ArrayHelper.add(selectedEvents, event);
            }
        }
        return selectedEvents;
    }

    private Event[] getEventsByLocation(){
        Location location = locationSelector();
        return location.getEvents();
    }

    private Event[] getEventsByDate(){
        LocalDateTime searchedDate = validationService.validateInputIsLocalDateTime("Please enter the date of the searched events: (DD.MM.YYYY HH:mm)");
        Event[] allEvents = new Event[0];
        for (Location location : locations) {
            ArrayHelper.addAll(location.eventsWhileDuration(searchedDate, 1, TimeUnit.DAY), allEvents);
        }
        return allEvents;
    }

    private void showMostUsedLocation(){
        Location mostUsedLocation = locations[0];
        for (Location location : locations) {
            if (location.getEvents().length > mostUsedLocation.getEvents().length){
                mostUsedLocation = location;
            }
        }
        System.out.printf("%nThe most used location is %s with %d events.%n", mostUsedLocation.getName(), mostUsedLocation.getEvents().length);
    }

    private void showLeastUsedLocation(){
        Location leastUsedLocation = locations[0];
        for (Location location : locations) {
            if (location.getEvents().length < leastUsedLocation.getEvents().length){
                leastUsedLocation = location;
            }
        }
        System.out.printf("%nThe least used location is %s with %d events.%n", leastUsedLocation.getName(), leastUsedLocation.getEvents().length);

    }

    private void printEvents(Event[] events){

    }

    private Location locationSelector(){
        for (int i = 0; i < locations.length; i++) {
            System.out.printf("%d: %s%n", i+1, locations[i].getName());
        }
        int locationPlace = validationService.validateInputIsInRange("Please enter the number of the location: ", 1, locations.length);
        return locations[locationPlace-1];
    }

}
