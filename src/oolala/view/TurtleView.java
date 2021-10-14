package oolala.view;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class TurtleView extends CreatureView {

    // Relevant for all Turtle objects that TurtleView is listening to:
    private Canvas turtlePathCanvas;
    private Canvas turtleStampCanvas;
    private GraphicsContext turtlePathGraphics;
    private GraphicsContext turtleStampGraphics;
    private double turtlePenWidth;
    private Paint turtlePenColor;

    // Relevant for only the current (most recent) Turtle object that TurtleView has heard from:
    private boolean currentTurtleIsWriting;
    private boolean currentTurtleIsVisible;

    /**
     * TurtleView's constructor - initializes Canvases, GraphicsContexts, and StackPane
     */
    public TurtleView(){
        super();
        turtlePathCanvas = new Canvas();
        turtleStampCanvas = new Canvas();
        turtlePathGraphics = turtlePathCanvas.getGraphicsContext2D();
        turtleStampGraphics = turtleStampCanvas.getGraphicsContext2D();
        currentCreatureImage = new Image(TURTLE_IMAGE_URL);
    }

    /**
     * Sets up the TurtleView display
     * @param width the desired width of the TurtleView Canvas
     * @param height the desired height of the TurtleView Canvas
     * @return a StackPane containing multiple Canvases needed to show the turtle images, paths, and stamps
     */
    @Override
    public StackPane setupDisplay(int width, int height) {
        canvasWidth = width;
        canvasHeight = height;
        creatureImageWidth = canvasWidth/CANVAS_TO_IMAGE_SIZE_RATIO;
        creatureImageHeight = canvasWidth/CANVAS_TO_IMAGE_SIZE_RATIO;
        resizeCanvas(turtlePathCanvas);
        resizeCanvas(turtleStampCanvas);
        myStackPane.setMaxSize(canvasWidth, canvasHeight);
        myStackPane.getChildren().addAll(turtlePathCanvas, turtleStampCanvas);
        return myStackPane;
    }

    /**
     * Sets desired color scheme for the TurtleView
     * @param penColor the desired color of the Turtle's pen/path
     * @param backgroundColor the desired color of the TurtleView Canvas
     */
    public void setColors(Paint penColor, Paint backgroundColor){
        turtlePenColor = penColor;
        turtlePathGraphics.setStroke(turtlePenColor);
        myStackPane.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void setCurrentCreatureImage(Image image){
        currentCreatureImage = image;
    }

    /**
     * @return the current turtle pen/path color
     */
    public Paint getPenColor(){
        return turtlePenColor;
    }

    /**
     * Sets the width of the turtle pen/path
     * @param width desired width of the turtle pen/path
     */
    public void setPenWidth(double width){
        turtlePenWidth = width;
        turtlePathGraphics.setLineWidth(turtlePenWidth);
    }

    /**
     * @return the current turtle pen/path width
     */
    public double getPenWidth(){
        return turtlePenWidth;
    }

    /**
     * Clears all TurtleView Canvases and resets the Canvas Graphics map
     */
    @Override
    public void clear(){
        turtlePathGraphics.clearRect(0, 0, canvasWidth, canvasHeight);
        turtleStampGraphics.clearRect(0, 0, canvasWidth, canvasHeight);
        for(Map.Entry element : graphicsMap.entrySet()){
            GraphicsContext currentTurtleImageGraphics = (GraphicsContext) element.getValue();
            clearCanvas(currentTurtleImageGraphics);
        }
        graphicsMap.clear();
    }

    /**
     * Handles notifications regarding changes in state to a Turtle object that TurtleView is listening to (through PropertyChangeListener)
     * @param evt PropertyChangeEvent object that contains the details of the change
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt){
        super.propertyChange(evt);

        String propertyName = evt.getPropertyName();
        if(propertyName.equals("Writing")){
            handleWritingEvent(evt);
        }
        if(propertyName.equals("Visibility")){
            handleVisibilityEvent(evt);
        }
        if(propertyName.equals("Stamp")){
            stampTurtleImage();
        }
    }

    @Override
    protected void handleMovementEvent(PropertyChangeEvent evt){
        double[] oldPosition = (double[]) evt.getOldValue();
        double[] convertedOldPosition = convertCoordinates(oldPosition);

        if(currentTurtleIsVisible){
            moveCurrentImage(currentCreaturePosition[0], currentCreaturePosition[1]);
        }
        if(currentTurtleIsWriting){
            turtlePathGraphics.strokeLine(convertedOldPosition[0], convertedOldPosition[1], currentCreaturePosition[0], currentCreaturePosition[1]);
        }
    }

    @Override
    protected void handleAngleEvent(PropertyChangeEvent evt){
        currentCreatureAngle = (double) evt.getNewValue();
        if(currentTurtleIsVisible){
            moveCurrentImage(currentCreaturePosition[0], currentCreaturePosition[1]);
        }
    }

    private void handleWritingEvent(PropertyChangeEvent evt){
        currentTurtleIsWriting = (boolean) evt.getNewValue();
    }

    private void handleVisibilityEvent(PropertyChangeEvent evt){
        currentTurtleIsVisible = (boolean) evt.getNewValue();
        if(currentTurtleIsVisible){
            moveCurrentImage(currentCreaturePosition[0], currentCreaturePosition[1]);
        }
        else{
            GraphicsContext turtleImageGraphics = graphicsMap.get(currentCreatureID);
            clearCanvas(turtleImageGraphics);
        }
    }

    private void stampTurtleImage(){
        ImageView turtleImageView = createCreatureImageView();
        turtleStampGraphics.drawImage(turtleImageView.snapshot(null, null), currentCreaturePosition[0] - creatureImageWidth /2, currentCreaturePosition[1] - creatureImageHeight /2);
    }
}
