/**
 * Represents a city.
 * @author Bora Yilmazer, Student ID: 2023400000
 * @since April 5th, 2024
 */
public class City {
    public String cityName;
    public int x;
    public int y;
    public boolean visited;
    public City theCityBefore;
    public double distanceFromDeparture;
    public City (String name, int x, int y){
        this.cityName = name;
        this.x = x;
        this.y = y;
        this.visited = false;
        this.theCityBefore = null;
        this.distanceFromDeparture = 1.79769313486231570e+308d; // set to the maximum possible double value to mimic infinity
    }
}
