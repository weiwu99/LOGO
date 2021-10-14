package oolala.model;

public abstract class Creature {

    public static final int FULL_ANGLE = 360;

    //unique identifier for this Turtle object
    protected String myID;
    //array indicating the current x-position at index 0 and the current y-position at index 1
    protected double[] myPosition;
    //indicates the current angle the turtle is facing
    protected double myAngle;

    /**
     * Constructor of Creature pen object
     * @param x starting x-position
     * @param y starting y-position
     */
    public Creature(double x, double y, String ID) {
        myID = ID;
        myPosition = new double[2];
        myPosition[0] = x;
        myPosition[1] = y;
        myAngle = 0;
    }

    /**
     * Moves the x-position and the y-position based on the distance moved and the angle the creature is facing
     * @param d the distance that will be covered in the specified angle direction
     * @return a two-element array indicating the current x-position and y-position
     */
    public double[] moveDistance(double d) {
        calibrateView();
        double[] newPosition = new double[2];
        newPosition[0] = myPosition[0] + d * Math.cos(Math.toRadians(myAngle));
        newPosition[1] = myPosition[1] - d * Math.sin(Math.toRadians(myAngle));
        sendViewUpdate("Movement", myPosition, newPosition);
        myPosition = newPosition;
        return newPosition;
    }

    /**
     * Changes the angle based off input angle
     * A positive angle stands for turning left
     * A negative angle stands for turning right
     * @param a angle change to be implemented
     * @return the modified angle
     */
    public double changeAngle(double a) {
        calibrateView();
        double oldAngle = myAngle;
        myAngle += a;
        if(myAngle >= FULL_ANGLE) {
            myAngle -= FULL_ANGLE * (int) (myAngle / FULL_ANGLE);
        }
        else if(myAngle < 0) {
            myAngle = FULL_ANGLE * ((int) (-myAngle / FULL_ANGLE) + 1) + myAngle;
        }
        sendViewUpdate("Angle", oldAngle, myAngle);
        return myAngle;
    }

    protected abstract void sendViewUpdate(String propertyName, Object oldValue, Object newValue);

    protected abstract void calibrateView();
}
