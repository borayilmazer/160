import java.util.ArrayList;
import java.util.Random;
/**
 * Represents an ant in an ant colony optimization algorithm for solving the traveling salesman problem.
 * An ant constructs a tour by moving from one building to another, attempting to find an optimal path.
 */
public class Ant {
    private double tourLength;// The total length of the tour
    private Building startingNode;// The starting building of the tour
    private Building currentBuilding;// The current building the ant is located at
    private ArrayList<Building> nodesToVisit;// The list of buildings yet to be visited
    private ArrayList<Building> tour;// The list of buildings visited in the tour

    private ArrayList<Connection> edgesPassed;// The list of connections passed during the tour

    /**
     * Constructs an Ant object with a starting node and a list of nodes to visit.
     *
     * @param startingNode The starting node of the tour.
     * @param nodesToVisit The list of nodes yet to be visited.
     */
    Ant(Building startingNode, ArrayList<Building> nodesToVisit){
        this.tourLength = 0;
        this.currentBuilding = startingNode;
        this.startingNode = startingNode;
        this.nodesToVisit = nodesToVisit;
        this.tour = new ArrayList<Building>();
        this.edgesPassed = new ArrayList<Connection>();
        nodesToVisit.remove(startingNode);
    }
    /**
     * Adds a building to the tour.
     *
     * @param building The building to add to the tour.
     */
    public void addBuilding(Building building){
        tour.add(building);
    }
    /**
     * Sets the total length of the tour based on the distances between buildings.
     */
    public void setTourLength(){
        double tourlength = 0;
        for (int i = 0; i < tour.size()-1; i++){
            tourlength = tourlength + distanceBetweenBuildings(tour.get(i),tour.get(i+1));
            this.tourLength = tourlength;

        }
    }
    /**
     * Gets the total length of the tour.
     *
     * @return The total length of the tour.
     */
    public double getTourLength(){
        return this.tourLength;
    }
    /**
     * Gets the list of connections passed during the tour.
     *
     * @return The list of connections passed during the tour.
     */
    public ArrayList<Connection> getEdgesPassed() {
        return edgesPassed;
    }
    /**
     * Adds a passed connection to the list of edges passed during the tour.
     *
     * @param c The connection to add.
     */
    public void addPassedEdge(Connection c){
        this.edgesPassed.add(c);
    }
    /**
     * Moves the ant to the next building based on probabilities of available connections.
     *
     * @param connections The list of available connections.
     */

    public void move(ArrayList<Connection> connections) {
        Random rand = new Random();
        double totalProbability = 0.0;
        double[] probabilities = new double[connections.size()];
        // Calculate probabilities for unvisited buildings
        for (int i = 0; i < connections.size(); i++) {
            Connection connection = connections.get(i);
            Building nextBuilding = connection.getNode2();
            // Check both directions of the connection
            if ((connection.getNode1() == currentBuilding || connection.getNode2() == currentBuilding) &&
                    nodesToVisit.contains(nextBuilding)) {
                double probability = connection.getEdgeWeight();
                probabilities[i] = probability;
                totalProbability += probability;
            }
        }

        // If there are no valid probabilities, return
        if (totalProbability == 0.0) {
            return;
        }

        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= totalProbability;
        }

        // Choose next building based on probabilities
        double randomValue = rand.nextDouble();
        double cumulativeProbability = 0.0;
        Building nextBuilding = null;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (randomValue <= cumulativeProbability) {
                nextBuilding = connections.get(i).getNode2();
                connections.get(i).findInverseConnection(connections);
                this.edgesPassed.add(connections.get(i));
                break;
            }
        }

        // If nextBuilding is still null, assign it to the first available building
        if (nextBuilding == null) {
            for (int i = 0; i < connections.size(); i++) {
                Connection connection = connections.get(i);
                Building building = connection.getNode2();
                if ((connection.getNode1() == currentBuilding || connection.getNode2() == currentBuilding) &&
                        nodesToVisit.contains(building)) {
                    nextBuilding = building;
                    break;
                }
            }
        }
        // Move to the next building and update tour
        currentBuilding = nextBuilding;
        tour.add(currentBuilding);
        nodesToVisit.remove(currentBuilding); // Mark the current building as visited
    }


    /**
     * Gets the list of buildings visited in the tour.
     *
     * @return The list of buildings visited in the tour.
     */
    public ArrayList<Building> getTour() {
        return tour;
    }
    /**
     * Gets the list of nodes yet to be visited.
     *
     * @return The list of nodes yet to be visited.
     */
    public ArrayList<Building> getNodesToVisit(){
        return this.nodesToVisit;
    }
    /**
     * Calculates the Euclidean distance between two buildings.
     *
     * @param b1 The first building.
     * @param b2 The second building.
     * @return The distance between the two buildings.
     */
    public static double distanceBetweenBuildings(Building b1, Building b2){
        double xDifference = Math.abs(b1.getxC() - b2.getxC());
        double yDifference = Math.abs(b1.getyC() - b2.getyC());
        return Math.sqrt((xDifference * xDifference) + (yDifference * yDifference));
    }
    /**
     * Calculates the total distance of a cycle through a sequence of buildings.
     *
     * @param sequence The sequence of buildings representing the tour.
     * @param buildings The list of buildings.
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
     * Gets the current building where the ant is located.
     *
     * @return The current building.
     */
    public Building getCurrentBuilding(){
        return this.currentBuilding;
    }


}
