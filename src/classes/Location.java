package classes;

public class Location {
    private String name;
    private int maxCapacity;

    public Location(String name, int maxCapacity){
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxCapacity() {
        return this.maxCapacity;
    }

}
