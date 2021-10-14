package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public class PenDownLogoCommand extends LogoCommand {

    protected boolean myPenDown;

    public PenDownLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        myPenDown = true;
    }

    @Override
    public int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles) {

        for (String name: getMyTurtleMap().keySet()) {
//            System.out.println(name);
            getMyTurtleMap().get(name).setWriting(myPenDown);
        }
        return index;
    }

    @Override
    public String getCommand() {
        return "pd";
    }
}
