package oolala.model.LogoCommands;

import oolala.model.Turtle;
import oolala.view.TurtleView;

import java.util.List;
import java.util.Map;

public class HomeLogoCommand extends LogoCommand {


    public HomeLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
    }

    @Override
    public int performAction(List<String> myTokens, int index, TurtleView view, Map<String, Turtle> inactiveTurtles) {
        for (String name: getMyTurtleMap().keySet()) {
//            System.out.println(name);
            getMyTurtleMap().get(name).setWriting(false);
            getMyTurtleMap().get(name).returnToOrigin();
            getMyTurtleMap().get(name).setWriting(true);
        }
        return index;
    }

    @Override
    public String getCommand() {
        return "home";
    }

}
