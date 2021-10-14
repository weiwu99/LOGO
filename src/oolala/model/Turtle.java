package oolala.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Turtle extends Creature {

    //indicates the starting position of the turtle
    protected double[] myOrigin;
    //writing indicates penUp/penDown
    protected boolean isWriting;
    //indicates whether the turtle icon is visible
    protected boolean isVisible;
    // object that allows Turtle to keep track of observers/listeners and notify them of changes in its state
    protected PropertyChangeSupport support;

    /**
     * Constructor of Turtle pen object
     * @param x starting x-position
     * @param y starting y-position
     */
    public Turtle(double x, double y, String ID) {
        super(x, y, ID);
        myOrigin = new double[2];
        myOrigin[0] = x;
        myOrigin[1] = y;
        isVisible = true;
        isWriting = true;

        support = new PropertyChangeSupport(this);
    }

    @Override
    protected void sendViewUpdate(String propertyName, Object oldValue, Object newValue){
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    protected void calibrateView(){
        sendViewUpdate("ID", "", myID);
        sendViewUpdate("Position", new double[2], myPosition);
        sendViewUpdate("Angle", null, myAngle);
        sendViewUpdate("Visibility", !isVisible, isVisible);
        sendViewUpdate("Writing", !isWriting, isWriting);
    }

    /**
     * Sets the turtle's home/origin location
     * @param x the x-coordinate of the desired home location
     * @param y the y-coordinate of the desired home location
     */
    public void setHome(double x, double y){
        myOrigin[0] = x;
        myOrigin[1] = y;
    }

    /**
     * @return boolean indicating whether the turtle is visible (true) or isn't visible (false)
     */
    public boolean getVisible(){
        return isVisible;
    }

    /**
     * @return boolean indicating whether the pen is up (false) or down (true)
     */
    public boolean getWriting(){
        return isWriting;
    }

    /**
     * Changes the visibility of the turtle (off/on toggle)
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
        sendViewUpdate("Visibility", !isVisible, isVisible);
    }

    /**
     * Changes the pen to the opposite position (if pen was up, pen goes down; if pen was down, pen goes up)
     */
    public void setWriting(boolean writing) {
        isWriting = writing;
        sendViewUpdate("Writing", !isWriting, isWriting);
    }

    /**
     * Changes the position of the turtle to a specified (x, y) position
     * @param x the new x-position of the turtle
     * @param y the new y-position of the turtle
     */
    public void teleport(double x, double y) {
        calibrateView();
        double[] newPosition = new double[2];
        newPosition[0] = x;
        newPosition[1] = y;
        sendViewUpdate("Movement", myPosition, newPosition);
        myPosition[0] = x;
        myPosition[1] = y;
    }

    /**
     * Returns the turtle back to the original position it was declared to when calling the constructor
     */
    public void returnToOrigin() {
        teleport(myOrigin[0], myOrigin[1]);
    }

    /**
     * Stamps an image of the turtle onto the Canvas at the turtle's current position
     */
    public void stamp(){
        calibrateView();
        sendViewUpdate("Stamp", false, true);
    }

    // ***** PropertyChange methods below: *****

    /**
     * Add an observer/listener to Turtle's instance of PropertyChangeSupport, which enables Turtle to notify that observer
     * of changes in its state. Also update the View on the Turtle's initial position
     * @param pcl the PropertyChangeListener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
        calibrateView();
    }

}
