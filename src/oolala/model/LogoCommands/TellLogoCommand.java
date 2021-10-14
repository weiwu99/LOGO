package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public class TellLogoCommand extends LogoCommand {

    private double[] turtleHome;

    public TellLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        turtleHome = new double[2];
        turtleHome[0] = 0;
        turtleHome[1] = 0;
    }

    @Override
    public int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles) throws NumberFormatException, NullPointerException {
        // set all previously active turtles to be inactive
        resetActiveTurtles(inactiveTurtles);
        // how many turtles we are activating
        int numOfTurtles = Integer.parseInt(myTokens.get(++index).trim());

        // loop through all active turtles required
        for(int i = 0; i < numOfTurtles; i++) {
            String name = myTokens.get(++index).trim(); //just to make sure
            // activate existing turtles
            if(inactiveTurtles.containsKey(name)) {
                activateToldTurtles(inactiveTurtles, name);
            }
            // if told turtle does not exist
            else {
                initializeNewTurtle(view, name);
            }
        }
        return index;
    }

    private void activateToldTurtles(Map<String, Turtle> inactiveTurtles, String name) {
        getMyTurtleMap().put(name, inactiveTurtles.get(name));
        inactiveTurtles.remove(name);
    }

    private void resetActiveTurtles(Map<String, Turtle> inactiveTurtles) {
        for (String name: getMyTurtleMap().keySet()) {
            inactiveTurtles.put(name, getMyTurtleMap().get(name));
        }
        getMyTurtleMap().clear();
    }

    private void initializeNewTurtle(TurtleView view, String name) {
        Turtle t = new Turtle(turtleHome[0], turtleHome[1], name);
        t.addPropertyChangeListener(view);
        getMyTurtleMap().put(name, t);
    }

    @Override
    public String getCommand() {
        Map<String, Turtle> activeTurtles = getMyTurtleMap();
        String result = "tell " + activeTurtles.size() + " ";
        for(String s : activeTurtles.keySet()) {
            result += s + " ";
        }
        return result;
    }


    /**
     * Sets the home location for new Turtle objects to be spawned at
     * @param x the x-coordinate of the desired home location
     * @param y the y-coordinate of the desired home location
     */

    public void setHome(double x, double y){
        turtleHome[0] = x;
        turtleHome[1] = y;
    }
}
