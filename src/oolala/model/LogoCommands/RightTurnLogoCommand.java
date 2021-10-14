package oolala.model.LogoCommands;

import oolala.model.Turtle;

import java.util.Map;

public class RightTurnLogoCommand extends LeftTurnLogoCommand {
    public RightTurnLogoCommand(Map<String, Turtle> activeTurtles) {
        super(activeTurtles);
        sign = -1;
    }

    @Override
    public String getCommand() {
        return "rt " + myAngle;
    }
}
