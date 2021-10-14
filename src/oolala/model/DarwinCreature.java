package oolala.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DarwinCreature extends Creature {

    private String mySpecies;
    private DarwinProgram myProgram;
    private int myProgramIndex;
    private double mySensingRange;

    // object that allows Turtle to keep track of observers/listeners and notify them of changes in its state
    protected PropertyChangeSupport support;


    public DarwinCreature(double x, double y, String ID, String species, DarwinProgram program, double range){
        super(x, y, ID);
        mySpecies = species;
        myProgram = program;
        myProgramIndex = 0;
        mySensingRange = range;

        support = new PropertyChangeSupport(this);
    }

    @Override
    protected void sendViewUpdate(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    protected void calibrateView() {
        sendViewUpdate("ID", "", myID);
        sendViewUpdate("Position", new double[2], myPosition);
        sendViewUpdate("Angle", null, myAngle);
        sendViewUpdate("Species", "", mySpecies);
    }

    /**
     * @return the species of this creature
     */
    public String getSpecies(){
        return mySpecies;
    }

    /**
     * @return the DarwinProgram that defines the creature
     */
    public DarwinProgram getProgram(){
        return myProgram;
    }

    /**
     * @return the index of the instruction that this creature is currently at in its program
     */
    public int getProgramIndex(){
        return myProgramIndex;
    }

    public void setProgramIndex(int i){
        myProgramIndex = i;
    }

    public double[] getPosition(){
        return myPosition;
    }

    public double getAngle(){
        return myAngle;
    }

    /**
     * @return the sensing range of the creature
     */
    public double getSensingRange(){
        return mySensingRange;
    }

    public void changeSpecies(String species, DarwinProgram newProgram){
        calibrateView();
        sendViewUpdate("Species", mySpecies, species);
        mySpecies = species;
        myProgram = newProgram;
        myProgramIndex = 0;
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

    /**
     * Remove an observer/listener from Turtle's instance of PropertyChangeSupport
     * @param pcl the PropertyChangeListener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl){
        support.removePropertyChangeListener(pcl);
    }
}
