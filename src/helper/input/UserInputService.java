package helper.input;

import java.util.Scanner;

public class UserInputService {
    //Create a Scanner object to read input from the user
    private final Scanner scanner = new Scanner(System.in);

    //Get a String Input from the user after printing a message
    public String getStringFromUserWithMessage(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
}
