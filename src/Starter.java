import classes.EventPlanner;
import classes.MenuHandler;
import helper.UserInputService;

public class Starter {

    public static void main(String[] args) {


        System.out.println("Hello on the event planner!");

        String demoDesicision = UserInputService.getStringFromUserWithMessage("Do you want to run the demo with predefined locations? (y/n): ");

        if (demoDesicision.equals("y")) {
            new MenuHandler(new EventPlanner()).runMenu();
        } else {
            int locationCount = UserInputService.getValidIntFromUser("For how many locations do you want to plan events? ");
            new MenuHandler(new EventPlanner(locationCount)).runMenu();
        }


    }

}