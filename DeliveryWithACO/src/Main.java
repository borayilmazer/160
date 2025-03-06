
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class represents a solution generator for the travelling salesman problem. Depending on some variables, the class solves the problem either by using brute force or ant colony optimization.
 * @author Bora Yilmazer
 * @version 1.0
 * @since 2024-05-13
 */
public class Main {
    public static void main(String[] args) {
        final int NUM_ANTS = 150;
        final int NUM_ITERATIONS = 80;
        final double ALPHA = 1; // Pheromone importance
        final double BETA = 1.6;  // Heuristic information importance
        final double EVAPORATION_RATE = 0.9;
        final double Q = 0.0001;
        final double pheromoneIntensity = 0.01;
        double startTime = System.currentTimeMillis();
        int chosenMethod = 2; // 1 for brute force, 2 for ant colony optimization
        int antColonyVisualization = 2; // if 2 is chosen for chosenMethod, 1 will display the shortest path, 2 will display the pheromone trails
        // Read the txt file, use the coordinates to generate Building objects, put these objects into an array list
        ArrayList<Building> buildings = new ArrayList<>();
        try {
            File file = new File("resources/inputs/input05.txt");  // copy the path of the desired input file.
            Scanner scanner = new Scanner(file);
            int idx = 0;
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                double xC = Double.parseDouble(parts[0]);
                double yC = Double.parseDouble(parts[1]);
                if (lineNumber == 1) {
                    Building migros = new Building(lineNumber,xC, yC, true);
                    buildings.add(migros);
                }
                else {
                    Building house = new Building(lineNumber,xC, yC, false);
                    buildings.add(house);
                    idx = idx + 1;
                }
                lineNumber = lineNumber+1;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        int numberOfBuildings = buildings.size();
        int[] sequence = new int[numberOfBuildings-1];
        for (int i=1; i<numberOfBuildings;i++){
            sequence[i-1] = i;
        }
        if (chosenMethod==1) { //Brute Force Method
            // create a solution object to store the minimum distance value and path.
            Solution solution = new Solution();
            shortestPathFinder(sequence, numberOfBuildings - 1, buildings, solution);
            int lengthPath = solution.getPath().length;
            int[] resultPath = new int[lengthPath+1];
            for (int i = 0; i < solution.getPath().length; i++){
                resultPath[i] = solution.getPath()[i].getIndex();
            }
            //Set the last Building in the resultPath array as the Migros of the bunch
            resultPath[lengthPath] = 1;
            System.out.println("Method: Brute-Force Method");
            double resultDistance = 0;

            for (int i=0; i < lengthPath-1; i++ ){
                resultDistance = resultDistance + distanceBetweenBuildings(solution.getPath()[i],solution.getPath()[i+1]);
            }
            System.out.println("Shortest distance: " + solution.getDistance());
            System.out.println(Arrays.toString(resultPath));
            double endTime = System.currentTimeMillis();
            double duration = (endTime - startTime)/1000;
            System.out.println("Time it takes to find the shortest path: " + duration + " seconds.");

            // StdDraw
            StdDraw.setCanvasSize(800,800);
            StdDraw.setXscale(0.0,1.0);
            StdDraw.setYscale(0.0,1.0);
            Font font = new Font("Helvetica", Font.PLAIN,17);
            StdDraw.setPenRadius(0.007);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.BLACK);
            // Draw the lines that make up the general route, excluding the return to Migros
            for (int i = 0; i<lengthPath-1; i++){
                StdDraw.line(solution.getPath()[i].getxC(), solution.getPath()[i].getyC(),solution.getPath()[i+1].getxC(),solution.getPath()[i+1].getyC());
            }
            // Draw the line that closes the loop
            StdDraw.line(solution.getPath()[lengthPath-1].getxC(), solution.getPath()[lengthPath-1].getyC(),solution.getPath()[0].getxC(),solution.getPath()[0].getyC());
            for (Building b: solution.getPath()){
                //use the draw method defined in the Building class
                b.draw();
            }
        }




        else if (chosenMethod==2) { // Ant Colony Optimization
            double minDistance = Double.MAX_VALUE;
            Solution solution = new Solution();
            //Create connection objects, append them to an array list

            ArrayList<Connection> connections = new ArrayList<>();
            for (int i = 0; i < buildings.size(); i++) {
                Building building1 = buildings.get(i);
                for (int j = 0; j < buildings.size(); j++) {
                    if (j != i) {
                        Building building2 = buildings.get(j);
                        double distance = distanceBetweenBuildings(building1, building2);
                        Connection connection = new Connection(building1, building2, distance, ALPHA,BETA, pheromoneIntensity);
                        connections.add(connection);
                    }
                }
            }
            // the ant traversals begin
            for (int i =0; i < NUM_ITERATIONS; i++){ // ITERATION COUNT
                for (int j =0; j < NUM_ANTS; j++){ // ANT COUNT
                    int index = (int) (Math.random() * (buildings.size() - 1)) + 1;

                    ArrayList<Connection> connectionsCopy = (ArrayList<Connection>) connections.clone();
                    ArrayList<Building> buildingsCopy = (ArrayList<Building>) buildings.clone();

                    Ant ant = new Ant(buildings.get(index), buildingsCopy);
                    ant.addBuilding(ant.getCurrentBuilding());
                    while (ant.getNodesToVisit().size() != 0) {
                        ant.move(connections);
                    }
                    ant.addBuilding(ant.getTour().get(0));
                    ant.setTourLength();
                    Connection finalConnection = findConnection(ant.getTour().get(ant.getTour().size()-2),ant.getTour().get(0),connectionsCopy);
                    ant.addPassedEdge(finalConnection);

                    double distance = 0;
                    for (Connection conn1: ant.getEdgesPassed()){
                        distance = distance + conn1.getLength();
                    }
                    for (Connection C: ant.getEdgesPassed()){
                        double delta = Q/distance;
                        C.increasePheromoneIntensity(delta);
                        C.updateEdgeWeight();

                    }
                    if (distance<minDistance){
                        Building[] tourSolution = new Building[ant.getTour().size()];
                        for (int m=0; m < ant.getTour().size(); m++){
                            tourSolution[m] = ant.getTour().get(m);
                        }
                        minDistance = distance;
                        solution.setDistance(minDistance);
                        solution.setPath(tourSolution);
                    }

                }

                //update pheromones
                for (Connection c: connections){
                    c.fadePheromone(EVAPORATION_RATE);
                    c.updateEdgeWeight();
                }
            }
            // create an array list that represents the path by pulling the indexes out of the buildings
            ArrayList<Integer> resultPath = new ArrayList<Integer>();
            for (Building b: solution.getPath()){
                resultPath.add(b.getIndex());
            }
            // convert resultPath to an array list that starts and ends at the Migros
            ArrayList<Integer> finalResultPath = new ArrayList<Integer>();
            int indexofOne = resultPath.indexOf(1);

            for (int p = indexofOne; p < resultPath.size(); p++){
                finalResultPath.add(resultPath.get(p));
            }
            for (int j = 1; j <indexofOne; j++){
                finalResultPath.add(resultPath.get(j));
            }
            finalResultPath.add(1);


            // Convert the arrayList to string
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (int element : finalResultPath) {

                stringBuilder.append(element).append(", ");
            }
            String result1 = stringBuilder.toString();
            if (result1.length() > 0) {
                result1 = result1.substring(0, result1.length() - 2);
            }
            result1 = result1 + "]";




            // Console Outout
            System.out.println("Method: Ant Colony Optimization Method");
            System.out.println("Shortest Distance: " + solution.getDistance());
            double endTime = System.currentTimeMillis();
            double timeDuration = (endTime - startTime)/1000;

            System.out.println("Path: " + result1);
            System.out.println("Time it takes to find the shortest path: " + timeDuration + " seconds.");
            // Display type - 2
            if (antColonyVisualization==2){
            StdDraw.setCanvasSize(800,800);
            StdDraw.setXscale(0.0,1.0);
            StdDraw.setYscale(0.0,1.0);
            Font font = new Font("Helvetica", Font.PLAIN,17);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.BLACK);
            // Draw the lines that make up the general route, excluding the return to Migros
            for (Connection c: connections){
                c.draw();
            }
            // Draw the line that closes the loop
            for (Building b: buildings) {
                //use the draw method defined in the Building class
                b.draw();
            }


            }
            //Display type - 1
            else if (antColonyVisualization==1){
                int lengthPath = solution.getPath().length;
                StdDraw.setCanvasSize(800,800);
                StdDraw.setXscale(0.0,1.0);
                StdDraw.setYscale(0.0,1.0);
                Font font = new Font("Helvetica", Font.PLAIN,17);
                StdDraw.setPenRadius(0.007);
                StdDraw.setFont(font);
                StdDraw.setPenColor(Color.BLACK);
                // Draw the lines that make up the general route, excluding the return to Migros
                for (int i = 0; i<lengthPath-1; i++){
                    StdDraw.line(solution.getPath()[i].getxC(), solution.getPath()[i].getyC(),solution.getPath()[i+1].getxC(),solution.getPath()[i+1].getyC());
                }
                // Draw the line that closes the loop
                StdDraw.line(solution.getPath()[lengthPath-1].getxC(), solution.getPath()[lengthPath-1].getyC(),solution.getPath()[0].getxC(),solution.getPath()[0].getyC());
                for (Building b: solution.getPath()) {
                    //use the draw method defined in the Building class
                    b.draw();
                }
            }
        }
    }
    /**
     * Calculates the Euclidean distance between two buildings represented by their coordinates.
     *
     * @param b1 The first building.
     * @param b2 The second building.
     * @return The Euclidean distance between the two buildings.
     */
    public static double distanceBetweenBuildings(Building b1, Building b2){
        double xDifference = Math.abs(b1.getxC() - b2.getxC());
        double yDifference = Math.abs(b1.getyC() - b2.getyC());
        return Math.sqrt((xDifference * xDifference) + (yDifference * yDifference));
    }
    /**
     * Calculates the total distance of a cycle passing through a sequence of buildings.
     *
     * @param sequence An array representing the sequence of buildings to visit.
     * @param buildings An ArrayList containing all buildings.
     * @return The total distance of the cycle.
     */
    public static double calculateCycle(int[] sequence, ArrayList<Building> buildings) {
        double distance = 0;
        distance = distance + distanceBetweenBuildings(buildings.get(0), buildings.get(sequence[0]));
        for (int i = 1; i < sequence.length; i++) {
            distance = distance + distanceBetweenBuildings(buildings.get(sequence[i - 1]), buildings.get(sequence[i]));
        }
        distance = distance + distanceBetweenBuildings(buildings.get(sequence[sequence.length - 1]), buildings.get(0));
        return distance;
    }
    /**
     * Recursively finds the shortest path by generating all permutations of building sequences.
     *
     * @param arr An array representing the current permutation of building indices.
     * @param size The size of the permutation array.
     * @param buildings An ArrayList containing all buildings.
     * @param solution The Solution object to store the best solution found.
     */
    private static void shortestPathFinder(int[] arr, int size, ArrayList<Building> buildings, Solution solution) {
        if (size == 1) {
            double distance = calculateCycle(arr, buildings);
            if (distance < solution.getDistance()) {
                // Create a new array to hold the permutation of buildings
                Building[] permutedBuildings = new Building[arr.length + 1];
                // Set the starting and ending building
                permutedBuildings[0] = buildings.get(0);
                permutedBuildings[permutedBuildings.length - 1] = buildings.get(0);
                // Set the buildings according to the permutation indices
                for (int i = 0; i < arr.length; i++) {
                    permutedBuildings[i + 1] = buildings.get(arr[i]);
                }
                solution.setDistance(distance);
                solution.setPath(permutedBuildings);
            }
            return;
        }
        for (int i = 0; i < size; i++) {
            shortestPathFinder(arr, size - 1, buildings, solution);
            if (size % 2 == 0) {
                swap(arr, i, size - 1);
            } else {
                swap(arr, 0, size - 1);
            }
        }
    }
    /**
     * Swaps two elements in an integer array.
     *
     * @param arr The integer array.
     * @param i Index of the first element.
     * @param j Index of the second element.
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    /**
     * Finds a connection between two buildings in a list of connections.
     *
     * @param from The starting building.
     * @param to The destination building.
     * @param connections The list of connections.
     * @return The connection between the two buildings, or null if no connection is found.
     */
    public static Connection findConnection(Building from, Building to, ArrayList<Connection> connections){
        for (Connection c: connections){
            if (c.getNode1().equals(from) && c.getNode2().equals(to)){
                return c;
            }
        }
        return null;
    }

    }


