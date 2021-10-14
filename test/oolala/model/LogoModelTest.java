package oolala.model;

import oolala.model.LogoCommands.LogoCommand;
import oolala.view.TurtleView;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.util.*;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class LogoModelTest extends DukeApplicationTest {

    private final String filepath = "data/examples/logo/telltest.txt";

    @Test
    public void testTokenUpdate() throws FileNotFoundException {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens(filepath);
        List<String> tokens = model.getMyTokens();
        assertTrue(tokens.get(0).equals("tell"));
        assertTrue(tokens.get(2).equals("turtle1"));
        assertTrue(tokens.get(4).equals("fd"));
    }

    @Test
    public void testSetTokens() throws FileNotFoundException {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens(filepath);
        List<String> inputList = new ArrayList<>();
        inputList.add("added successfully");
        model.setMyTokens(inputList);
        assertTrue(model.getMyTokens().equals(inputList));
    }

    @Test
    public void testCommands() throws FileNotFoundException {
        TurtleView view = new TurtleView();
        LogoProgrammingModel model = new LogoProgrammingModel(view);
        model.updateTokens(filepath);
        Map<String, LogoCommand> map = model.getCommandMap();

        boolean canGetCommand = true;
        for (String key: map.keySet()) {
            if (!map.get(key).getCommand().split(" ")[0].equals(key)) {
                canGetCommand = false;
                break;
            }
        }
        assertTrue(canGetCommand);
    }

    @Test
    public void testResetTokens() throws FileNotFoundException {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens(filepath);
        model.resetTokens();
        assertTrue(model.getMyTokens().size() == 0);
    }

    @Test
    public void testTokenToString() throws FileNotFoundException {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens("data/examples/logo/forward.txt");
        boolean thrown = false;
        try {
            model.tokenToString(1).equals("tell 1 turtle");
        } catch (NullPointerException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testMovements() throws Exception {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens(filepath);
        model.tokenToAction(11);
        Map<String, Turtle> active = model.getActiveTurtles();
        for(String s : active.keySet()) {
            assertTrue(active.get(s).changeAngle(0) == 90);
        }
        model.tokenToAction(13);
        active = model.getActiveTurtles();
        for(String s : active.keySet()) {
            double[]values = active.get(s).moveDistance(0);
            assertTrue(values[0] == 0 && values[1] == 100);
        }
        model.tokenToAction(16);
        active = model.getActiveTurtles();
        for(String s : active.keySet()) {
            assertTrue(active.get(s).changeAngle(0) == 270);
        }
        model.tokenToAction(18);
        for(String s : active.keySet()) {
            double[]values = active.get(s).moveDistance(0);
            assertTrue(values[0] == 0 && values[1] == 200);
        }
    }

    @Test
    public void testVisuals() throws Exception {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens(filepath);
        model.tokenToAction(26);
        Map<String, Turtle> active = model.getActiveTurtles();
        for(String s : active.keySet()) {
            assertTrue(active.get(s).getWriting());
        }
        model.tokenToAction(29);
        for(String s : active.keySet()) {
            assertFalse(active.get(s).getWriting());
        }
        model.tokenToAction(32);
        for(String s : active.keySet()) {
            assertFalse(active.get(s).getVisible());
        }
        model.tokenToAction(35);
        for(String s : active.keySet()) {
            assertTrue(active.get(s).getVisible());
        }
        model.tokenToAction(38);
        for(String s : active.keySet()) {
            double[]values = active.get(s).moveDistance(0);
            assertTrue(values[0] == 0 && values[1] == 0);
        }
        model.tokenToAction(65);
        active = model.getActiveTurtles();
        for(String s : active.keySet()) {
            assertTrue(active.get(s).getVisible());
        }
    }

    @Test
    public void testHome() throws Exception {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens(filepath);
        model.tokenToAction(13);
        Map<String, Turtle> active = model.getActiveTurtles();
        for(String s : active.keySet()) {
            double[]values = active.get(s).moveDistance(0);
            assertTrue(values[0] == 0 && values[1] == 100);
        }
        model.tokenToAction(18);
        for(String s : active.keySet()) {
            double[]values = active.get(s).moveDistance(0);
            assertTrue(values[0] == 0 && values[1] == 200);
        }
    }

    @Test
    public void testTell() throws Exception {
        LogoProgrammingModel model = new LogoProgrammingModel(new TurtleView());
        model.updateTokens(filepath);
        runAsJFXAction(() -> {
            try {
                model.tokenToAction(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Map<String, Turtle> active = model.getActiveTurtles();
        assertTrue(active.containsKey("turtle1"));
        assertTrue(active.containsKey("turtle2"));
        model.tokenToAction(8);
        active = model.getActiveTurtles();
        Map<String, Turtle> inactive = model.getInactiveTurtles();
        assertTrue(inactive.containsKey("turtle2"));
        assertTrue(active.containsKey("turtle1"));
        assertFalse(active.containsKey("turtle2"));
    }
}
