import classes.Location;
import classes.event.Event;
import helper.input.UserInputService;
import helper.validation.ValidationService;

import java.util.Arrays;
import java.util.Scanner;

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

    public EventPlanner(int locationCount){
        locations = new Location[locationCount];
    }

    public void runPlanner(){
        this.locations = setupLocations();
        System.out.println(Arrays.toString(this.locations));

        //While loop with MenuHandlerRun Call and switch case with return value
    }

    private Location[] setupLocations(){
        System.out.printf("%nPlease enter the name and max capacity of %d locations:", this.locations.length);
        for (int i = 0; i < locations.length; i++) {
            String name = userInputService.getStringFromUserWithMessage(String.format("%nPlease enter the name of location %d: ", i+1));
            int maxCapacity = validationService.validateInputIsInt(String.format("Please enter the max. capacity of %s: ", name));
            locations[i] = new Location(name, maxCapacity);
        }
        return locations;
    }

    private void createNewEvent(){
        //TODO: implement this method
    }

    private Event[] getAllEvents(){
        return null;
    }

    private Event[] getEventsByName(){
        return null;
    }

    private Event[] getEventsByLocation(){
        return null;
    }

    private Event[] getEventsByDate(){
        return null;
    }

    private void showMostUsedLocation(){

    }

    private void showLeastUsedLocation(){

    }

    private void printEvents(Event[] events){

    }

}
