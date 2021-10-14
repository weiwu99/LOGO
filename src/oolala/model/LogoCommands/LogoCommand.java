package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public abstract class LogoCommand {

    private Map<String, Turtle> myTurtleMap;

    public LogoCommand(Map<String, Turtle> activeTurtles) {
        myTurtleMap = activeTurtles;
    }

    public abstract int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles) throws NumberFormatException, NullPointerException;

    public Map<String, Turtle> getMyTurtleMap() {
        return myTurtleMap;
    }

    public abstract String getCommand();
}
