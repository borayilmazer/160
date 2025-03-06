import java.util.ArrayList;
/**
 * Represents a connection between two buildings in a network.
 * This connection includes information about its length, pheromone intensity,
 * and edge weight, along with references to the two buildings it connects.
 */
public class Connection {
    private double a; // Pheromone importance
    private double b;
    private Building node1;
    private Building node2;
    private double length;
    private double pheromoneIntensity;
    private double edgeWeight;
    /**
     * Constructs a Connection object with the specified attributes.
     *
     * @param b1 The first building.
     * @param b2 The second building.
     * @param length The length of the connection.
     * @param a The alpha exponent.
     * @param b The beta exponent.
     * @param intensity The initial pheromone intensity.
     */

    Connection(Building b1, Building b2, double length, double a, double b,double intensity){
        this.a = a;
        this.b = b;
        this.node1 = b1;
        this.node2 = b2;
        this.length = length;
        this.pheromoneIntensity = intensity;
        this.edgeWeight = (Math.pow(this.pheromoneIntensity, a) / Math.pow(this.length, b));
    }
    /**
     * Updates the edge weight of the connection based on current pheromone intensity and length.
     */
    public void updateEdgeWeight(){
        this.edgeWeight =(Math.pow(this.pheromoneIntensity, a) / Math.pow(this.length, b));
    }
    /**
     * Retrieves the length of the connection.
     *
     * @return The length of the connection.
     */
    public double getLength(){
        return this.length;
    }
    /**
     * Increases the pheromone intensity of the connection by the specified amount.
     *
     * @param delta The amount by which to increase the pheromone intensity.
     */
    public void increasePheromoneIntensity(double delta){
        this.pheromoneIntensity=this.pheromoneIntensity + delta;
    }
    /**
     * Fades the pheromone intensity of the connection by the specified evaporation rate.
     *
     * @param evaporationRate The rate at which the pheromone intensity fades.
     */
    public void fadePheromone(double evaporationRate){
        this.pheromoneIntensity = this.pheromoneIntensity*evaporationRate;
    }
    /**
     * Retrieves the edge weight of the connection.
     *
     * @return The edge weight of the connection.
     */
    public double getEdgeWeight() {
        return edgeWeight;
    }
    /**
     * Retrieves the first building in the connection.
     *
     * @return The first building in the connection.
     */
    public Building getNode1() {
        return node1;
    }
    /**
     * Retrieves the second building in the connection.
     *
     * @return The second building in the connection.
     */
    public Building getNode2() {
        return node2;
    }
    /**
     * Checks if this connection is equal to another connection.
     *
     * @param connection The connection to compare with.
     * @return true if the connections connect identical buildings, false otherwise.
     */
    public boolean equals(Connection connection){
        boolean b = (node1.equals(connection.getNode1()) && node2.equals(connection.getNode2())) || (node1.equals(connection.getNode2()) && node2.equals(connection.getNode1()));
        return b;
    }
    /**
     * Returns a string representation of the connection.
     *
     * @return A string representation of the connection.
     */
    public String toString(){
        return "node1: " + node1.getIndex() + " node2: " + node2.getIndex() ;
    }
/**
 * Finds the inverse connection of this connection in the given list of connections.
 *
 * @param connections The list of connections to search in.
 * @return The inverse connection if found, null otherwise.
 */
    public Connection findInverseConnection(ArrayList<Connection> connections){
        Building b1 = this.node1;
        Building b2 = this.node2;
        for (Connection c: connections){
            if (c.getNode1().equals(b2) && c.getNode2().equals(b1)){
                return c;
            }
        }
        return null;
    }
    /**
     * Draws the connection on the screen using StdDraw.
     * The thickness of the line drawn is based on the pheromone intensity.
     */
    public void draw(){
        StdDraw.setPenColor(StdDraw.BLACK);
        if (this.pheromoneIntensity > 0.0004){
        StdDraw.setPenRadius(this.pheromoneIntensity*0.5);
        StdDraw.line(this.node1.getxC(),this.node1.getyC(),this.node2.getxC(),this.node2.getyC());}

    }

}
