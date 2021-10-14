package oolala.model.LogoCommands;

import oolala.model.Turtle;

import java.util.Map;

public class PenUpLogoCommand extends PenDownLogoCommand {

    public PenUpLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        myPenDown = false;
    }

    @Override
    public String getCommand() {
        return "pu";
    }
}
