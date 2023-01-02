import classes.EventPlanner;
import classes.MenuHandler;
import classes.helper.UserInputService;

public class Starter {

    public static void main(String[] args) {

        // Greet the user
        System.out.println("Hello on the event planner!");

        // Ask the user for the amount of locations he wants to create
        int locationCount = UserInputService.getValidIntFromUser("For how many locations do you want to plan events? ");
        new MenuHandler(new EventPlanner(locationCount)).runMenu();


    }

}