/**
 * The Solution class represents a solution to a problem involving a path through buildings.
 * It encapsulates information about the path and its distance.
 */
public class Solution {
    private Building[] path;
    private double distance;
    /**
     * Constructs a new Solution object with an uninitialized path and a maximum distance.
     */
    Solution(){
        this.distance = Double.MAX_VALUE;
        this.path = null;
    }
    /**
     * Sets the path for this solution.
     *
     * @param path The array of buildings representing the path
     */
    public void setPath(Building[] path){
        this.path = path;
    }
    /**
     * Retrieves the path of this solution.
     *
     * @return The array of buildings representing the path
     */
    public Building[] getPath(){
        return this.path;
    }
    /**
     * Sets the distance for this solution.
     *
     * @param distance The distance of the path
     */
    public void setDistance(double distance){
        this.distance = distance;
    }
    /**
     * Retrieves the distance of this solution.
     *
     * @return The distance of the path
     */
    public double getDistance(){
        return this.distance;
    }
}
