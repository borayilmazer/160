import java.awt.event.KeyEvent;
import java.awt.*;

/** A basic two-dimensional target shooting game
 * @author Bora Yilmazer, Student ID: 2023400000
 * @since Date: March 20th, 2024
 */
public class AngryBullets {

    public static void main(String[] args) {

        // bunch of parameters that are defined with their default values
        int width = 1600; // screen width
        int height = 800; // screen height
        double gravity = 9.80665; // gravity
        double x0 = 120; // initial x coordinate for the bullet
        double y0 = 120; // initial y coordinate for the bullet
        double bulletVelocity = 180; // default bullet velocity
        double bulletAngle = 45.0; // default bullet angle
        double radianTheta = Math.toRadians(bulletAngle); // the conversion of the bullet angle from degrees to radians
        double cosTheta = Math.cos(radianTheta); // the cosine value for the bullet angle
        double sinTheta = Math.sin(radianTheta); // the sine value for the bullet angle
        int keyboardPauseDuration = 45;
        int count = 0; // count to be incremented by one in each loop
        boolean gamePaused ;
        double[][] obstacleArray = { // default obstacles. custom ones are left as a comment below.
                {1200, 0, 60, 220},
                {1000, 0, 60, 160},
                {600, 0, 60, 80},
                {600, 180, 60, 160},
                {220, 0, 120, 180}
        };
        // the custom obstacle array
//        double[][] obstacleArray = {
//                {800,0,50,290},
//                {1200, 0, 50, 150},
//                {1200, 190, 50, 160},
//                {1200, 400, 50, 60},
//                {1200, 500, 50, 100},
//                {1200, 720, 50, 20},
//        };

        double[][] targetArray = { // default targets. custom ones are left as a comment below.
                {1160, 0, 30, 30},
                {730, 0, 30, 30},
                {150, 0, 20, 20},
                {1480, 0, 60, 60},
                {340, 80, 60, 30},
                {1500, 600, 60, 60}
       };

         //the custom target array
//        double[][] targetArray = {
//                {850, 0, 40, 40}, // this one is for the bravest
//                {150, 680, 5, 5},
//                {1200, 150, 50, 40},
//                {1200, 350, 50, 50},
//                {1350, 400, 50, 60},
//                {1200, 700, 50, 20}
//
//        };

        // Set up the canvas
        StdDraw.setCanvasSize(width,height);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.enableDoubleBuffering();

        // the main game loop
        while (true) {
            StdDraw.clear(); // clear the board before drawing new frames

            // map the arrow keys to alter the bullet angle and velocity
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                StdDraw.pause(keyboardPauseDuration);
                bulletVelocity = bulletVelocity - 1;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
                StdDraw.pause(keyboardPauseDuration);
                bulletVelocity = bulletVelocity + 1;
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                StdDraw.pause(keyboardPauseDuration);
                bulletAngle = bulletAngle + 1;
                // recalculate the trigonometric values according to the new bullet angle
                radianTheta = Math.toRadians(bulletAngle);
                cosTheta = Math.cos(radianTheta);
                sinTheta = Math.sin(radianTheta);
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                StdDraw.pause(keyboardPauseDuration);
                bulletAngle = bulletAngle - 1;
                // recalculate the trigonometric values according to the new bullet angle
                radianTheta = Math.toRadians(bulletAngle);
                cosTheta = Math.cos(radianTheta);
                sinTheta = Math.sin(radianTheta);
            }

            // draw the shooting platform and the shooting line
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.filledRectangle(60,60,60,60);
            StdDraw.line(120,120, 120 + bulletVelocity*cosTheta/3.0,120 + bulletVelocity*sinTheta/3.0);

            // display the bullet velocity and bullet angle as text within the shooting platform
            StdDraw.setFont( new Font("Helvetica", Font.BOLD, 20));
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(20, 65, "a: " + bulletAngle);
            StdDraw.textLeft(20, 35, "v: " + bulletVelocity);

            // draw the obstacles
            StdDraw.setPenColor(Color.DARK_GRAY);
            for (double[] obs : obstacleArray) {
                double[] convertedObs = cornerToCenter(obs);
                StdDraw.filledRectangle(convertedObs[0], convertedObs[1], convertedObs[2], convertedObs[3]);
            }

            // draw the targets
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            for (double[] targets : targetArray) {
                double[] convertedTarg = cornerToCenter(targets);
                StdDraw.filledRectangle(convertedTarg[0], convertedTarg[1], convertedTarg[2], convertedTarg[3]);
            }

            StdDraw.show();
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE)) {
                double time = 0.0; // reset the timer
                double xt;
                double yt;
                double xOld = 120.0; // default coordinates for the previous circle on the display
                double yOld = 120.0;
                StdDraw.pause(keyboardPauseDuration);
                boolean gameRuns = true;
                while (gameRuns) {
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.filledCircle(120, 120, 4); // draw the first circle at the upper right corner of the shooting platform.
                    // increase the time and count. the rate of increase for the time (0.05) is chosen via trial and error.
                    count = count + 1;
                    time = time + 0.05;
                    double scaledVelocity = bulletVelocity/1.750;  // scaling the velocity by  1.750 is decided by trial and error to better mimic the gameplay video
                    // calculate the current x and y coordinates of the bullet
                    xt = x0 + scaledVelocity * time * cosTheta;
                    yt = y0 + (scaledVelocity * time * sinTheta) - (gravity * time * time / 2);

                    if (count % 5 == 0) { // take further action every five loops. the integer 5 is chosen via trial and error.

                        StdDraw.filledCircle(xt, yt, 4);
                        StdDraw.setPenRadius(0.001);
                        StdDraw.line(xOld,yOld, xt,yt);

                        // check whether the bullet hits the ground
                        if (yt < 0) {
                            // get into the pause loop
                            gamePaused = true;
                            while (gamePaused) {
                                StdDraw.setFont( new Font("Helvetica", Font.BOLD, 20));
                                StdDraw.textLeft(10, 770, "Hit the ground. Press 'r' to shoot again.");
                                StdDraw.show();
                                if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                                    StdDraw.pause(100);
                                    // return parameters to the default values and break out of the pause loop
                                    bulletVelocity = 180;
                                    bulletAngle = 45.0;
                                    radianTheta = Math.toRadians(bulletAngle);
                                    cosTheta = Math.cos(radianTheta);
                                    sinTheta = Math.sin(radianTheta);
                                    break;
                                }
                            }
                            // break out of the loop that runs the simulation
                            break;
                        }

                        // check whether the bullet hits the target
                        boolean hitTarget = inTarget(xt, yt, targetArray);
                        if (hitTarget){
                            // get into the pause loop
                            gamePaused = true;
                            while (gamePaused) {
                                StdDraw.textLeft(10, 770, "Congratulations: You hit the target!");
                                StdDraw.show();
                                if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                                    StdDraw.pause(100);
                                    // return parameters to the default values and break out of the pause loop
                                    bulletVelocity = 180;
                                    bulletAngle = 45.0;
                                    radianTheta = Math.toRadians(bulletAngle);
                                    cosTheta = Math.cos(radianTheta);
                                    sinTheta = Math.sin(radianTheta);
                                    break;
                                }
                            }
                            // break out of the loop that runs the simulation
                            break;
                        }
                        // check whether the bullet hits an obstacle
                        boolean hitObstacle = inTarget(xt, yt, obstacleArray);
                        if (hitObstacle){
                            // get into the pause loop
                            gamePaused = true;
                            while (gamePaused) {
                                StdDraw.textLeft(10, 770, "Hit an obstacle. Press 'r' to shoot again.");
                                StdDraw.show();
                                if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                                    StdDraw.pause(100);
                                    // return parameters to the default values and break out of the pause loop
                                    bulletVelocity = 180;
                                    bulletAngle = 45.0;
                                    radianTheta = Math.toRadians(bulletAngle);
                                    cosTheta = Math.cos(radianTheta);
                                    sinTheta = Math.sin(radianTheta);
                                    break;
                                }
                            }
                            // break out of the loop that runs the simulation
                            break;
                        }
                        // check whether the bullet has reached the max x level
                        if (xt > 1600) {
                            // get into the pause loop
                            gamePaused = true;
                            while (gamePaused) {
                                StdDraw.textLeft(10, 770, "Max X reached. Press 'r' to shoot again.");
                                StdDraw.show();
                                if (StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                                    StdDraw.pause(100);
                                    // return parameters to the default values and break out of the pause loop
                                    bulletVelocity = 180;
                                    bulletAngle = 45.0;
                                    radianTheta = Math.toRadians(bulletAngle);
                                    cosTheta = Math.cos(radianTheta);
                                    sinTheta = Math.sin(radianTheta);
                                    break;
                                }
                            }
                            // break out of the loop that runs the simulation
                            break;
                        }
                        // assign the coordinates of the bullet to other variables to be used to draw lines in the next loop
                        xOld = xt;
                        yOld = yt;
                    }
                    StdDraw.show(); // display the image
                    StdDraw.pause(2);
                }
            }
        }
    }

    // a method to convert the given target/obstacle arrays to ones with parameters that can be used with the filledRectangle method.
    // from the given (x and y coordinates of the lower left rectangle corner, width, and height) arrangement to the (x and y coordinates of the center of the rectangle, half the width, and half the height) version.

    /**
     * Modifies an array
     * @param arr An array specifying a rectangle. Contains four elements regarding the rectangle - {x coordinate of the lower left corner, y coordinate of the lower left corner, width, height}
     * @return A new array that contains four elements - {x coordinate of the center, y coordinate of the center, half the width of the rectangle, half the length of the rectangle}
     */
    public static double[] cornerToCenter(double[] arr){
        double[] newParameters;
        newParameters = new double[4];
        newParameters[0] = arr[0] + arr[2]/2;
        newParameters[1] = arr[1] + arr[3]/2;
        newParameters[2] = arr[2]/2;
        newParameters[3] = arr[3]/2;
        return newParameters;
    }


    /**
     * Checks whether a point with coordinates (xC,yC) is within a rectangle designated by an array of obstacle/target arrays
     * @param xC The x-component of the coordinate
     * @param yC The y-component of the coordinate
     * @param rectArray An array of arrays. Each row has four elements: {x coordinate of the lower left corner, y component of the lower left corner, width, height}
     * @return true if the point is in one of the rectangles specified in rectArray, false if isn't in any
     */
    // a method to check whether a point with coordinates (xC,yC) is within a rectangle designated by an array of obstacle/target arrays.
    public static boolean inTarget(double xC, double yC, double[][] rectArray){
        boolean inTarget = false; // default return parameter
        int lengthofArray = rectArray.length; // find the number of rectangles that are going to be checked.
        for (int i = 0; i < lengthofArray; i++) {
            double minX = rectArray[i][0]; // take the minimum x coordinate of the rectangle
            double minY = rectArray[i][1]; // take the minimum y coordinate of the rectangle
            double maxX = rectArray[i][0] + rectArray[i][2]; // add the width of the rectangle to minX to get the maximum x value
            double maxY = rectArray[i][1] + rectArray[i][3]; // add the height of the rectangle to minY to get the maximum y value
            if ((xC > minX && xC < maxX) && (yC > minY && yC < maxY)) {
                inTarget = true;
            }
        }
        return inTarget;
    }
}