package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public class StampLogoCommand extends LogoCommand {
    public StampLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
    }

    @Override
    public int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles) {
        for (String name: getMyTurtleMap().keySet()) {
            getMyTurtleMap().get(name).stamp();
        }
        return index;
    }

    @Override
    public String getCommand() {
        return "stamp";
    }
}
