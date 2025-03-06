import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.Font;

/** A basic path finding application for a simplified map of Turkey
 * @author Bora Yilmazer, Student ID: 2023400000
 * @since Date: April 5th, 2024
 */

public class Main {

    public static void main(String[] args){

        // Create a new array list of City objects using the data from the "city_coordinates.txt" file.
        ArrayList<City> cityList = new ArrayList<>();
        try {
            FileInputStream file1 = new FileInputStream("resources/city_coordinates.txt");
            Scanner scanner = new Scanner(file1);
            int i=0;
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] lineSplit = line.split(", ");
                String cityName = lineSplit[0];
                int cityX = Integer.parseInt(lineSplit[1]);
                int cityY = Integer.parseInt(lineSplit[2]);
                City newCity = new City(cityName, cityX, cityY);
                cityList.add(newCity);
                i = i + 1;
            }
            scanner.close();
        }
        catch(
                IOException e)
        {
            e.printStackTrace();
        }

        // Create another new array list. This time, it is two-dimensional, each containing one "connection" - the names of two connected cities.
        ArrayList<ArrayList<String>> connections = new ArrayList<>();
        try {
            FileInputStream file2 = new FileInputStream("resources/city_connections.txt");
            Scanner scanner = new Scanner(file2);
            int i = 0;
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] lineSplit = line.split(",");
                ArrayList<String> templist = new ArrayList<>();
                for (String str : lineSplit){
                    templist.add(str);
                }
                connections.add(templist);
                i = i + 1;
            }
            scanner.close();
        }
        catch(
                IOException e)
        {
            e.printStackTrace();
        }

        // Declare the names of the departure and arrival cities
        String departure;
        String arrival;

        // Take the city names as input, locate them in the array list "cityList".
        while (true){
            Scanner input1 = new Scanner(System.in);
            System.out.print("Enter starting city: ");
            departure = input1.nextLine();
            if (cityExists(departure,cityList)){ // Check the cityList array list for the city.
                break;
            }
            else {
                System.out.println("City named '" + departure +"' not found. Please enter a valid city name. ");
            }

        }
        while (true) {
            Scanner input2 = new Scanner(System.in);
            System.out.print("Enter destination city: ");
            arrival = input2.nextLine();
            if (cityExists(arrival,cityList)){
                break;
            }
            else {
                System.out.println("City named '" + arrival +"' not found. Please enter a valid city name. ");
            }
        }

        // using the name attribute, find the two cities from the array list "cityList".
        City departureCity = fetchCity(departure, cityList);
        City arrivalCity = fetchCity(arrival,cityList);

        // use the "allDistances" method to come up with a two dimensional array list.
        // This array list uses the indexes of the cities in cityList to display the distances between the cities.
        // for the same index in both dimensions, we set 0.0
        // for cities that are not connected, we set -1.0

        ArrayList<ArrayList<Double>> twoDdistances = allDistances(cityList,connections);
        departureCity.distanceFromDeparture = 0; // the city's distance from itself is set to zero.
        int numberOfCities = cityList.size();
        for (int i = 0; i < numberOfCities ; i++) {
            City closestCity = null; // Initially, the closest city to the departure city is left blank.

            // Iterate through the cities. If a city is not visited AND if a closest city to the departure city is unknown / if the city's distance from the departure city is shorter than the existing closest city's distance to the departure city, set the city as the new closest city.
            for (City y : cityList) {
                if(!y.visited && (closestCity == null || y.distanceFromDeparture < closestCity.distanceFromDeparture )){
                    closestCity = y;
                }
            }

            // set the closest city as visited, take it out of the makeshift queue
            closestCity.visited = true;

            // Iterate through the cities one more time. If the city is connected to the closest city AND its distance from the departure city is larger than the sum of its distance to the closest city and the distance between it and the closest city, set this sum as the city's distance from the departure city.
            for (City y : cityList) {
                double lengthOfConnection = twoDdistances.get(cityList.indexOf(closestCity)).get(cityList.indexOf(y));
                if (lengthOfConnection != -1.0 && closestCity.distanceFromDeparture + lengthOfConnection < y.distanceFromDeparture){
                    y.distanceFromDeparture = closestCity.distanceFromDeparture + lengthOfConnection;
                    y.theCityBefore = closestCity;
                }
            }
        }

        // Iterate through the cities, starting from the arrival city. Follow the preceding cities, add them in to an array list that will be used to draw the path.
        ArrayList<City> path = new ArrayList<>();
        City current = arrivalCity;
        while (current != null){
            path.add(current);
            current = current.theCityBefore;
        }

        // If the distance between the arrival and departure cities is still the maximum double value, we can infer that they are not connected.
        if (arrivalCity.distanceFromDeparture == 1.79769313486231570e+308d){
            System.out.print("No path could be found.");
        }
        else{
            // Print the distance value
            System.out.printf(Locale.US,"Total Distance: %.2f"+ ". ", arrivalCity.distanceFromDeparture); // The "Locale.US" part was found online. I couldn't change the locale the way described in the assignment discussion forum.
            // Print path
            System.out.print("Path: ");
            for (int i = path.size() - 1; i >= 0; i--) {
                City pathCity = path.get(i);
                System.out.print(pathCity.cityName);
                if (i != 0) {
                    System.out.print(" -> ");
                }
            }

            // Usual procedure for the StdDraw library
            StdDraw.setCanvasSize(2377, 1055);
            StdDraw.setXscale(0,2377);
            StdDraw.setYscale(0,1055);
            StdDraw.enableDoubleBuffering();
            StdDraw.picture(2377.0/2.0, 1055.0/2.0, "resources/map.png");

            // Draw the default map
            StdDraw.setPenColor(StdDraw.GRAY);
            for (City city : cityList) {
                drawCity(city);
            }
            for (ArrayList<String> twoCities : connections){
                connect(twoCities, cityList);
            }

            // Draw the route and cities in light blue
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            for (City city : path){
                drawCity(city);
            }
            StdDraw.setPenRadius(0.02);
            for (int j = 0; j < path.size()-1; j++){
                StdDraw.line(path.get(j).x, path.get(j).y, path.get(j+1).x, path.get(j+1).y);
            }
            StdDraw.show();
        }
    }

    /**
     * Draws a city.
     * @param city the city that is to be drawn
     */
    public static void drawCity(City city){
        int xC = city.x;
        int yC = city.y;
        String nameC = city.cityName;
        StdDraw.setFont( new Font("Helvetica", Font.PLAIN, 20));
        StdDraw.filledCircle(xC, yC, 5);
        StdDraw.text(xC, yC+15 ,nameC );
    }

    /**
     * Finds whether a city with a specific cityName attribute exists within an array list of cities
     * @param name the desired cityName
     * @param cities_list an array list of cities
     * @return true if the city with cityName name is present in cities_list, false otherwise
     */
    public static boolean cityExists(String name, ArrayList<City> cities_list){
        boolean exists = false;
        for (City city : cities_list){
            if (city.cityName.equals(name)){
                exists = true;
            }
        }
        return exists;
    }
    /**
     * Returns a City object that has the given string as its cityName attribute. Used in conjunction with the cityExists method.
     * @param name the name of the city that is being searched
     * @param cities_list the array list of cities that is KNOWN to contain a City c so that c.cityName = name
     * @return City foundCity that has the given string as its cityName attribute.
     */
    public static City fetchCity(String name, ArrayList<City> cities_list){
        City foundCity = null;
        for (City city : cities_list){
            if (city.cityName.equals(name)){
                foundCity = city;
            }
        }
            return foundCity;
    }

    /**
     * Calculates the distance between two cities.
     * @param city1 a City object
     * @param city2 another City object
     * @return the distance between the cities
     */
    public static double distance(City city1, City city2){
        int xDiff = Math.abs(city1.x - city2.x);
        int yDiff = Math.abs(city1.y - city2.y);
        double result = Math.sqrt((double) xDiff*xDiff + yDiff*yDiff);
        return result;
    }

    /**
     * Returns a two dimensional array list that contains the distances between two cities, and -1.0 if the cities are not connected.
     * @param cities an array list of cities
     * @param connections an array list of array lists. Each sub array list contains the cityNames of two cities.
     * @return distances - a 2D array list
     */
    public static ArrayList<ArrayList<Double>> allDistances(ArrayList<City> cities, ArrayList<ArrayList<String>> connections){
        ArrayList<ArrayList<Double>> distances = new ArrayList<>(cities.size());
        for (int i = 0; i < cities.size(); i++) {
            distances.add(new ArrayList<>(cities.size()));
            for (int j = 0; j < cities.size(); j++) {
                distances.get(i).add(-1.0); // Initially set distances to max double
            }
        }
        for (ArrayList<String> connection : connections){
            City city1 = fetchCity(connection.get(0), cities);
            City city2 = fetchCity(connection.get(1), cities);
            int index1 = cities.indexOf(city1);
            int index2 = cities.indexOf(city2);
            double dist = distance(city1,city2);
            distances.get(index1).set(index2, dist);
            distances.get(index2).set(index1, dist);
        }
        for (City city : cities){
            int index = cities.indexOf(city);
            distances.get(index).set(index, 0.0);
        }
        return distances;
    }

    /**
     * Draws a line between two connected cities.
     * @param oneLine an array list of length 2 that contains the names of two connected cities.
     * @param allCities an array list of cities
     */
    public static void connect(ArrayList<String> oneLine, ArrayList<City> allCities){
        City city1 = fetchCity(oneLine.get(0), allCities);
        City city2 = fetchCity(oneLine.get(1), allCities);
        int x1 = city1.x;
        int y1 = city1.y;
        int x2 = city2.x;
        int y2 = city2.y;
        StdDraw.line(x1, y1, x2, y2);
    }
}