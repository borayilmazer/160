import java.awt.*;
/**
 * The Building class represents a building object with attributes such as index, coordinates, and whether it contains a Migros store.
 * It provides methods to access and modify these attributes, as well as to draw the building on a canvas.
 */
public class Building {
    private int index;
    private double xC;
    private double yC;
    private boolean isMigros;
    /**
     * Constructs a new Building object with the specified index, coordinates, and Migros status.
     *
     * @param index the index of the building
     * @param xC the X-coordinate of the building's center
     * @param yC the Y-coordinate of the building's center
     * @param isMigros true if the building contains a Migros store, false otherwise
     */
    Building(int index,double xC, double yC, boolean isMigros){
        this.index = index;
        this.xC = xC;
        this.yC = yC;
        this.isMigros = isMigros;
    }
    /**
     * Returns the index of the building.
     *
     * @return the index of the building
     */
    public int getIndex(){
        return this.index;
    }
    /**
     * Sets the index of the building.
     *
     * @param index the new index of the building
     */
    public void setIndex(int index){
        this.index = index;
    }
    /**
     * Sets the X-coordinate of the building's center.
     *
     * @param x the new X-coordinate of the building's center
     */
    public void setxC (double x){
        this.xC = x;
    }
    /**
     * Returns the X-coordinate of the building.
     *
     * @return the X-coordinate of the building.
     */
    public double getxC() {
        return xC;
    }
    /**
     * Returns the Y-coordinate of the building.
     *
     * @return the Y-coordinate of the building.
     */
    public double getyC() {
        return yC;
    }
    /**
     * Returns a string representation of the building.
     *
     * @return a string representation of the building
     */

    public String toString(){
        return String.valueOf(index);
    }
    /**
     * Checks if this building is equal to another building based on their indices.
     *
     * @param building the building to compare
     * @return true if the buildings have the same index, false otherwise
     */
    public boolean equals(Building building){
        return (this.index == building.getIndex());
    }
    /**
     * Draws the building on the canvas.
     * If the building contains a Migros store, it is filled with Princeton Orange color; otherwise, it is filled with Light Gray color.
     * The index of the building is displayed at its center in black color.
     */
    public void draw(){
        if (this.isMigros){
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        }
        else {
            StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
        }
        StdDraw.filledCircle(this.xC,this.yC, 0.02);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.text(this.xC,this.yC, String.valueOf(this.index));
    }
}
