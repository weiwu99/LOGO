package oolala.view;

import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DarwinEnvironmentView extends CreatureView implements PropertyChangeListener {

    // Relevant for all DarwinCreature objects that DarwinEnvironmentView is listening to:
    private Image newSpeciesImage;
    private Map<String, Image> speciesImageMap;

    // Relevant for only the current (most recent) DarwinCreature object that DarwinEnvironmentView has heard from:
    private String currentCreatureSpecies;

    public DarwinEnvironmentView(){
        super();
        speciesImageMap = new HashMap<>();
        newSpeciesImage = new Image(TURTLE_IMAGE_URL);
    }

    @Override
    public StackPane setupDisplay(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;
        creatureImageWidth = canvasWidth/CANVAS_TO_IMAGE_SIZE_RATIO;
        creatureImageHeight = canvasWidth/CANVAS_TO_IMAGE_SIZE_RATIO;
        myStackPane.setMaxSize(canvasWidth, canvasHeight);
        return myStackPane;
    }

    /**
     * Sets desired color for the DarwinEnvironmentView
     * @param backgroundColor the desired color of the TurtleView Canvas
     */
    public void setColor(Paint backgroundColor){
        myStackPane.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Sets the image to be displayed for the next new Darwin species that is created
     * @param image the desired image
     */
    public void setNewSpeciesImage(Image image){
        newSpeciesImage = image;
    }

    /**
     * Clears all DarwinEnvironmentView Canvases and resets the Canvas Graphics map
     */
    @Override
    public void clear() {
        for(Map.Entry element : graphicsMap.entrySet()){
            GraphicsContext currentTurtleImageGraphics = (GraphicsContext) element.getValue();
            clearCanvas(currentTurtleImageGraphics);
        }
        graphicsMap.clear();
        currentCreatureAngle = 0;
    }

    /**
     * Handles notifications regarding changes in state to a DarwinCreature object that DarwinEnvironmentView is listening to (through PropertyChangeListener)
     * @param evt PropertyChangeEvent object that contains the details of the change
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        String propertyName = evt.getPropertyName();
        if(propertyName.equals("Species")){
            handleSpeciesEvent(evt);
        }
    }

    @Override
    protected void handleMovementEvent(PropertyChangeEvent evt) {
        moveCurrentImage(currentCreaturePosition[0], currentCreaturePosition[1]);
    }

    @Override
    protected void handleAngleEvent(PropertyChangeEvent evt) {
        currentCreatureAngle = (double) evt.getNewValue();
        moveCurrentImage(currentCreaturePosition[0], currentCreaturePosition[1]);
    }

    private void handleSpeciesEvent(PropertyChangeEvent evt){
        currentCreatureSpecies = (String) evt.getNewValue();
        currentCreatureImage = getSpeciesImage(currentCreatureSpecies);
        moveCurrentImage(currentCreaturePosition[0], currentCreaturePosition[1]);
    }

    private Image getSpeciesImage(String species){
        speciesImageMap.putIfAbsent(species, newSpeciesImage);
        return speciesImageMap.get(species);
    }
}
