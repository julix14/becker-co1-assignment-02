import classes.EventPlanner;
import classes.MenuHandler;
import helper.input.UserInputService;
import helper.validation.ValidationService;

public class Starter {
    private final static ValidationService VALIDATION_SERVICE = new ValidationService();
    private final static UserInputService USER_INPUT_SERVICE = new UserInputService();

    public static void main(String[] args) {


        System.out.println("Hello on the event planner!");

        String demoDesicision = USER_INPUT_SERVICE.getStringFromUserWithMessage("Do you want to run the demo with predefined locations? (y/n): ");

        if (demoDesicision.equals("y")) {
            new MenuHandler(new EventPlanner()).runMenu();
        } else {
            int locationCount = VALIDATION_SERVICE.getValidIntFromUser("For how many locations do you want to plan events? ");
            new MenuHandler(new EventPlanner(locationCount)).runMenu();
        }


    }

}