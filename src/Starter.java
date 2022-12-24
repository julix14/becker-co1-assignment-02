import java.util.Scanner;

public class Starter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello on the event planner!");
        System.out.print("For how many locations do you want to plan events? ");
        int locationCount = scanner.nextInt();

        new EventPlanner(locationCount).runPlanner();
    }


}