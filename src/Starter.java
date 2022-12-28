import classes.EventPlanner;
import classes.MenuHandler;
import helper.validation.ValidationService;

public class Starter {
    public static void main(String[] args) {
        ValidationService validationService = new ValidationService();

        System.out.println("Hello on the event planner!");
        int locationCount = validationService.getValidIntFromUser("For how many locations do you want to plan events? ");

        new MenuHandler(new EventPlanner(locationCount)).runMenu();
    }
}