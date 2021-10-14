package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public class ShowTurtleLogoCommand extends LogoCommand {

    protected boolean isVisible;

    public ShowTurtleLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        isVisible = true;
    }

    @Override
    public int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles) {

        for (String name: getMyTurtleMap().keySet()) {
//            System.out.println(name);
            getMyTurtleMap().get(name).setVisible(isVisible);
        }
        return index;
    }

    @Override
    public String getCommand() {
        return "st";
    }
}
