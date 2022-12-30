import classes.EventPlanner;
import classes.MenuHandler;
import helper.UserInputService;

public class Starter {

    public static void main(String[] args) {

        // Greet the user
        System.out.println("Hello on the event planner!");

        // Ask if the user wants to use a demo modus with predefined locations and events
        String demoDesicision = UserInputService.getStringFromUserWithMessage("Do you want to run the demo with predefined locations? (y/n): ");

        // If the user wants to use the demo modus, create a new EventPlanner with predefined locations and events
        if (demoDesicision.equals("y")) {
            new MenuHandler(new EventPlanner()).runMenu();
        } else {
            // If the user doesn't want to use the demo modus, create a new EventPlanner without predefined locations and events
            // And ask the user for the amount of locations he wants to create
            int locationCount = UserInputService.getValidIntFromUser("For how many locations do you want to plan events? ");
            new MenuHandler(new EventPlanner(locationCount)).runMenu();
        }


    }

}