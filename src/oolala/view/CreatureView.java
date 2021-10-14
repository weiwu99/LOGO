package oolala.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public abstract class CreatureView implements PropertyChangeListener {
    public static final int ANGLE_OFFSET = 90;
    public static final int CANVAS_TO_IMAGE_SIZE_RATIO = 20;
    //public static final String TURTLE_IMAGE_URL = "file:./data/turtle_image.jpeg";
    public static final String TURTLE_IMAGE_URL = "file:./oolala_team17/data/turtle_image.jpeg";

    protected StackPane myStackPane;

    // Relevant for all Creature objects that CreatureView is listening to:
    protected int canvasHeight;
    protected int canvasWidth;
    protected Map<String, GraphicsContext> graphicsMap;
    protected int creatureImageWidth;
    protected int creatureImageHeight;

    // Relevant for only the current (most recent) Creature object that CreatureView has heard from:
    protected Image currentCreatureImage;
    protected String currentCreatureID;
    protected double[] currentCreaturePosition;
    protected double currentCreatureAngle;

    public CreatureView() {
        graphicsMap = new HashMap<>();
        myStackPane = new StackPane();
        currentCreaturePosition = new double[2];
    }

    public abstract StackPane setupDisplay(int width, int height);

    /**
     * Clears the CreatureView display (clears all the Canvases in myStackPane) and resets the Canvas Graphics map
     */
    public abstract void clear();

    protected void resizeCanvas(Canvas myCanvas){
        myCanvas.setWidth(canvasWidth);
        myCanvas.setHeight(canvasHeight);
    }

    /**
     * Handles notifications regarding changes in state to a Creature object that CreatureView is listening to (through PropertyChangeListener)
     * @param evt PropertyChangeEvent object that contains the details of the change
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if(propertyName.equals("ID")){
            handleIDEvent(evt);
        }
        if(propertyName.equals("Position") || propertyName.equals("Movement")){
            updateCurrentPosition(evt);
            if(propertyName.equals("Movement")){
                handleMovementEvent(evt);
            }
        }
        if(propertyName.equals("Angle")){
            handleAngleEvent(evt);
        }
    }

    protected void handleIDEvent(PropertyChangeEvent evt) {
        currentCreatureID = (String) evt.getNewValue();
        if (!graphicsMap.containsKey(currentCreatureID)) {
            Canvas newCanvas = new Canvas();
            resizeCanvas(newCanvas);
            myStackPane.getChildren().add(newCanvas);
            graphicsMap.put(currentCreatureID, newCanvas.getGraphicsContext2D());
        }
    }

    protected void updateCurrentPosition(PropertyChangeEvent evt) {
        double[] newPosition = (double[]) evt.getNewValue();
        currentCreaturePosition = convertCoordinates(newPosition);
    }

    protected double[] convertCoordinates(double[] coordinates) {
        double[] result = new double[2];
        result[0] = coordinates[0] + canvasWidth / 2;
        result[1] = coordinates[1] + canvasHeight / 2;
        return result;
    }

    protected abstract void handleMovementEvent(PropertyChangeEvent evt);

    protected abstract void handleAngleEvent(PropertyChangeEvent evt);

    protected void moveCurrentImage(double newXPosition, double newYPosition){
        GraphicsContext creatureImageGraphics = graphicsMap.get(currentCreatureID);
        clearCanvas(creatureImageGraphics);
        ImageView creatureImageView = createCreatureImageView();
        creatureImageGraphics.drawImage(creatureImageView.snapshot(null, null), newXPosition - creatureImageWidth /2, newYPosition - creatureImageHeight /2);
    }

    protected ImageView createCreatureImageView(){
        ImageView turtleImageView = new ImageView();
        turtleImageView.setImage(currentCreatureImage);
        turtleImageView.setRotate(ANGLE_OFFSET - currentCreatureAngle);
        turtleImageView.setFitWidth(creatureImageWidth);
        turtleImageView.setFitHeight(creatureImageHeight);
        return turtleImageView;
    }

    protected void clearCanvas(GraphicsContext currentImageGraphics){
        currentImageGraphics.clearRect(0, 0, canvasWidth, canvasHeight);
    }
}
