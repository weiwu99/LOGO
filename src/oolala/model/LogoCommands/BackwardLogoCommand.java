package oolala.model.LogoCommands;

import oolala.model.Turtle;

import java.util.Map;

public class BackwardLogoCommand extends ForwardLogoCommand {
    public BackwardLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        sign = -1;
    }

    @Override
    public String getCommand() {
        return "bk " + myDistance;
    }
}
