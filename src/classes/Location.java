package classes;

public class Location {
    private String name;
    private int maxCapacity;

    public Location(String name, int maxCapacity){
        this.name = name;
        this.maxCapacity = maxCapacity;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
