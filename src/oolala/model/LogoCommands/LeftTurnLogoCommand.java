package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public class LeftTurnLogoCommand extends LogoCommand {

    protected double myAngle;
    protected int sign;

    public LeftTurnLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        myAngle = 0;
        sign = 1;
    }

    @Override
    public int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles)  throws NumberFormatException, NullPointerException {
        String angleStr = myTokens.get(++index);
        myAngle = Double.parseDouble(angleStr.trim());
        for (String name: getMyTurtleMap().keySet()) {
            getMyTurtleMap().get(name).changeAngle(myAngle*sign);
        }
        return index;
    }

    @Override
    public String getCommand() {
        return "lt " + myAngle;
    }

}
