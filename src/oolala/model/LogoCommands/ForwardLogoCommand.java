package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public class ForwardLogoCommand extends LogoCommand {

    protected double myDistance;
    protected int sign;

    public ForwardLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        myDistance = 0;
        sign = 1;
    }

    @Override
    public int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles) throws NumberFormatException, NullPointerException {
        String distStr = myTokens.get(++index);
        myDistance = Double.parseDouble(distStr.trim());
        for (String name: getMyTurtleMap().keySet()) {
            getMyTurtleMap().get(name).moveDistance(myDistance*sign);
        }
        return index;
    }

    @Override
    public String getCommand() {
        return "fd " + myDistance;
    }
}
