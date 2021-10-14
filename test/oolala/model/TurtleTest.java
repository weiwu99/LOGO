package oolala.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TurtleTest {

    public Turtle t = new Turtle(200, 200, "Turtle1");

    @Test
    public void changePenTest() {
        t.setWriting(false);
        assertFalse(t.getWriting());
        t.setWriting(true);
        assertTrue(t.getWriting());
    }

    @Test
    public void moveDistanceTest() {
        double[] arr = t.moveDistance(100);
        assertTrue(arr[0] == 300 && arr[1] == 200);
    }

    @Test
    public void setVisibleTest() {
        t.setVisible(false);
        assertFalse(t.getVisible());
        t.setVisible(true);
        assertTrue(t.getVisible());
    }

    @Test
    public void changeAngleTest() {
        double result = 0;
        assertTrue(t.changeAngle(-30) == 330);
        result = t.changeAngle(480);
        assertTrue(result == 90);
        double[] arr = t.moveDistance(100);
        assertTrue(arr[0] == 200 && arr[1] == 100);
    }

    @Test
    public void setWidthTest() {
        //assertTrue(t.setPenWidth(5) == 5);
    }

    @Test
    public void returnToOriginTest() {
        t.moveDistance(100);
        t.returnToOrigin();
        double[]cur = t.moveDistance(0);
        assertTrue(cur[0] == 200 && cur[1] == 200);
    }

    @Test
    public void teleportTest() {
        t.teleport(400, 100);
        double[]cur = t.moveDistance(0);
        assertTrue(cur[0] == 400 && cur[1] == 100);
    }
}
