package helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserInputService {
    //Create a Scanner object to read input from the user
    private static final Scanner SCANNER = new Scanner(System.in);

    //Get a String Input from the user after printing a message
    public static String getStringFromUserWithMessage(String message) {
        System.out.println(message);
        return SCANNER.nextLine();
    }

    public static int getValidIntFromUser(String message) {
        String input;
        int tries = 0;
        do {
            //Get a String Input from the user
            input = getStringFromUserWithMessage(message);

            //To assert the input is an Integer, try to parse it to an Integer
            //If so, return the Integer
            //If not, print an error message and ask for input again
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input is not a number");
            }
            tries++;
        }while (tries < 3);
        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return -1;
    }

    public static int getValidIntInRangeFromUser(String message, int min, int max) {
        String input;
        int tries = 0;
        do {
            //Get a String Input from the user
            input = getStringFromUserWithMessage(message);

            // Remove leading zeros
            input = input.replaceFirst("^0+(?!$)", "");

            //Check if the input is a number and in the range
            if (!input.matches("^[0-9]+$")) {
                System.out.println("Input is not a number, please enter a number between " + min + " and " + max);
            } else if (!(min <= Integer.parseInt(input) && Integer.parseInt(input) <= max)) {
                System.out.println("Input is not in range, please enter a number between " + min + " and " + max);
            } else {
                return Integer.parseInt(input);
            }
            tries++;
        }while (tries < 3);

        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return -1;
    }

    public static LocalDateTime getValidLocalDateTimeFromUser(String message) {
        String input;
        int tries = 0;
        do {
            //Get a String Input from the user
            input = getStringFromUserWithMessage(message);

            //To assert the input is an LocalDateTime, try to parse it to an LocalDateTime
            //If so, return the LocalDateTime
            //If not, print an error message and ask for input again
            try {
                return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Input is not a date");
            }
            tries++;
        }while (tries < 3);
        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return null;
    }

    public static LocalDate getValidLocalDateFromUser(String message) {
        String input;
        int tries = 0;
        do {
            //Get a String Input from the user
            input = getStringFromUserWithMessage(message);

            //To assert the input is an LocalDate, try to parse it to an LocalDate
            //If so, return the LocalDate
            //If not, print an error message and ask for input again
            try {
                return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Input is not a date");
            }
            tries++;
        }while (tries < 3);
        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return null;
    }
}
