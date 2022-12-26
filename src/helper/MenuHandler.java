package helper;

public class MenuHandler {
    private final String[] MENUITEMS;

    public MenuHandler(String[] MENUITEMS) {
        this.MENUITEMS = MENUITEMS;
    }

    public int runMenu() {
        return 0;
    }

    private void printMenu() {
        for (int i = 0; i < MENUITEMS.length; i++) {
            System.out.printf("[%d] - %s%n", i + 1, MENUITEMS[i]);
        }
    }

    private int selectMenuItem(){
        System.out.println();
    }
}
