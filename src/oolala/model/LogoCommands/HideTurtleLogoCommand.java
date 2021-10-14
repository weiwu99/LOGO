package oolala.model.LogoCommands;

import oolala.model.Turtle;

import java.util.Map;

public class HideTurtleLogoCommand extends ShowTurtleLogoCommand {
    public HideTurtleLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        isVisible = false;
    }

    @Override
    public String getCommand() {
        return "ht";
    }
}
