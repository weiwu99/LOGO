package oolala.model;

import oolala.model.FileReader.FileReader;
import oolala.view.DarwinEnvironmentView;

import java.io.FileNotFoundException;
import java.util.*;

public class DarwinSimulatorModel {

    public static final int CREATURE_VISION_ANGLE = 30;

    private DarwinEnvironmentView myEnvironmentView;
    private int environmentWidth;
    private int environmentHeight;
    private Map<String, List<String>> speciesToTokensMap;
    private double[] spawnLocation;
    private double sensingRange;
    private List<DarwinCreature> myCreatures;
    private int creatureCount;
    private Random myRandom;

    public DarwinSimulatorModel(DarwinEnvironmentView view, int width, int height){
        myEnvironmentView = view;
        environmentWidth = width;
        environmentHeight = height;
        creatureCount = 0;
        speciesToTokensMap = new HashMap<>();
        myCreatures = new ArrayList<>();
        spawnLocation = new double[2];
        myRandom = new Random();
    }

    public boolean createCreatureOfNewSpecies(String programFilePath, String species) throws FileNotFoundException {
        if(speciesToTokensMap.containsKey(species)){
            return false;
        }
        FileReader userTexts = new FileReader(programFilePath);
        userTexts.processInputs();
        List<String> tokens = userTexts.getTokens();
        speciesToTokensMap.put(species, tokens);
        boolean successfulCreation = createNewCreature(species, tokens);
        return successfulCreation;
    }

    public boolean createCreatureOfExistingSpecies(String species){
        List<String> tokens = speciesToTokensMap.get(species);
        boolean successfulCreation = createNewCreature(species, tokens);
        return successfulCreation;
    }

    private boolean createNewCreature(String species, List<String> tokens){
        if(checkPositionIsInBounds(spawnLocation) && checkPositionIsUnoccupied(spawnLocation)){
            creatureCount++;
            DarwinProgram program = new DarwinProgram(tokens);
            String ID = "" + creatureCount;
            DarwinCreature creature = new DarwinCreature(spawnLocation[0], spawnLocation[1], ID, species, program, sensingRange);
            creature.addPropertyChangeListener(myEnvironmentView);
            myCreatures.add(creature);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Sets the location for new DarwinCreature objects to be spawned at
     * @param x the x-coordinate of the desired spawn location
     * @param y the y-coordinate of the desired spawn location
     */
    public void setSpawn(double x, double y){
        spawnLocation[0] = x;
        spawnLocation[1] = y;
    }

    /**
     * Sets the radius/distance at which a new DarwinCreature can sense its environment (walls and other creatures)
     * @param r the desired radius
     */
    public void setSensingRange(double r){
        sensingRange = r;
    }

    public void step(){
        Collections.shuffle(myCreatures);
        for(DarwinCreature currentCreature : myCreatures){
            DarwinProgram program = currentCreature.getProgram();
            int programIndex = currentCreature.getProgramIndex();
            boolean running = true;
            while(running){
                String command = program.getCommand(programIndex);
                double argument = 0;
                if(program.getArgument(programIndex) != null){
                    argument = program.getArgument(programIndex);
                }
                if(command.equals("MOVE")){
                    running = false;
                    if(checkPositionAheadIsAvailable(currentCreature, argument)){
                        currentCreature.moveDistance(argument);
                    }
                }
                else if(command.equals("LEFT")){
                    currentCreature.changeAngle(argument);
                    running = false;
                }
                else if(command.equals("RIGHT")){
                    currentCreature.changeAngle(-1 * argument);
                    running = false;
                }
                else if(command.equals("INFECT")){
                    implementInfectCommand(currentCreature);
                    running = false;
                }
                else if(command.equals("IFEMPTY") && checkPositionNearbyAndAheadIsEmpty(currentCreature)){
                    programIndex = (int) argument - 2;
                }
                else if(command.equals("IFWALL") && !checkPositionNearbyAndAheadIsInBounds(currentCreature)){
                    programIndex = (int) argument - 2;
                }
                else if(command.equals("IFSAME") && checkSameIsNearbyAndAhead(currentCreature)){
                    programIndex = (int) argument - 2;
                }
                else if(command.equals("IFENEMY") && checkEnemyIsNearbyAndAhead(currentCreature)){
                    programIndex = (int) argument - 2;
                }
                else if(command.equals("IFRANDOM")){
                    if(myRandom.nextInt(99) < 50){
                        programIndex = (int) argument - 2;
                    }
                }
                else if(command.equals("GO")){
                    programIndex = (int) argument - 2;
                }
                programIndex++;
            }
            currentCreature.setProgramIndex(programIndex);
        }
    }

    private boolean checkPositionAheadIsAvailable(DarwinCreature currentCreature, double d){
        double[] positionAhead = calculatePositionAhead(currentCreature, d);
        boolean positionAheadIsInBounds = checkPositionIsInBounds(positionAhead);
        boolean positionAheadIsUnoccupied = checkPositionIsUnoccupied(positionAhead);

        return positionAheadIsInBounds && positionAheadIsUnoccupied;
    }

    private double[] calculatePositionAhead(DarwinCreature currentCreature, double d){
        double[] position = currentCreature.getPosition();
        double angle = currentCreature.getAngle();

        double[] positionAhead = new double[2];
        positionAhead[0] = position[0] + d * Math.cos(Math.toRadians(angle));
        positionAhead[1] = position[1] - d * Math.sin(Math.toRadians(angle));

        return positionAhead;
    }

    private boolean checkPositionIsInBounds(double[] position){
        boolean insideXBounds = (position[0] >= (-environmentWidth/2) && position[0] <= (environmentWidth/2));
        boolean insideYBounds = (position[1] >= (-environmentHeight/2) && position[1] <= (environmentHeight/2));
        return insideXBounds && insideYBounds;
    }

    private boolean checkPositionIsUnoccupied(double[] position){
        for(DarwinCreature creature : myCreatures) {
            double[] creaturePosition = creature.getPosition();
            if(creaturePosition[0] == position[0] && creaturePosition[1] == position[1]){
                return false;
            }
        }
        return true;
    }

    private void implementInfectCommand(DarwinCreature currentCreature){
        for(DarwinCreature otherCreature : myCreatures){
            if(otherCreature != currentCreature && !otherCreature.getSpecies().equals(currentCreature.getSpecies())){
                double[] otherCreaturePosition = otherCreature.getPosition();
                if(checkOtherCreatureIsNearbyAndAhead(currentCreature, otherCreaturePosition)){
                    otherCreature.changeSpecies(currentCreature.getSpecies(), currentCreature.getProgram());
                }
            }
        }
    }

    private boolean checkOtherCreatureIsNearbyAndAhead(DarwinCreature currentCreature, double[] otherCreaturePosition){
        double[] position = currentCreature.getPosition();
        double angle = currentCreature.getAngle();
        double range = currentCreature.getSensingRange();

        double[] rangeLimit1 = new double[2];
        double[] rangeLimit2 = new double[2];
        rangeLimit1[0] = position[0] + range * Math.cos(Math.toRadians(angle+CREATURE_VISION_ANGLE));
        rangeLimit1[1] = position[1] - range * Math.sin(Math.toRadians(angle+CREATURE_VISION_ANGLE));
        rangeLimit2[0] = position[0] + range * Math.cos(Math.toRadians(angle-CREATURE_VISION_ANGLE));
        rangeLimit2[1] = position[1] - range * Math.sin(Math.toRadians(angle-CREATURE_VISION_ANGLE));

        List<double[]> nearbyAndAheadTriangle = new ArrayList<>();
        nearbyAndAheadTriangle.add(position);
        nearbyAndAheadTriangle.add(rangeLimit1);
        nearbyAndAheadTriangle.add(rangeLimit2);

        return checkPointIsInsideTriangle(otherCreaturePosition, nearbyAndAheadTriangle);

    }

    private boolean checkPointIsInsideTriangle(double[] point, List<double[]> triangle){
        if(triangle.size() != 3){
            return false;
        }

        double[] trianglePoint1 = triangle.get(0);
        double[] trianglePoint2 = triangle.get(1);
        double[] trianglePoint3 = triangle.get(2);

        double triangleArea = calculateTriangeArea(trianglePoint1, trianglePoint2, trianglePoint3);
        double subtriangle1Area = calculateTriangeArea(point, trianglePoint2, trianglePoint3);
        double subtriangle2Area = calculateTriangeArea(point, trianglePoint1, trianglePoint3);
        double subtriangle3Area = calculateTriangeArea(point, trianglePoint1, trianglePoint2);

        return (triangleArea == (subtriangle1Area + subtriangle2Area + subtriangle3Area));
    }

    private double calculateTriangeArea(double[] a, double[] b, double[] c){
        return Math.abs( ( (a[0]*(b[1]-c[1])) + (b[0]*(c[1] - a[1])) + (c[0]*(a[1] - b[1]) ) ) / 2.0);
    }

    private boolean checkPositionNearbyAndAheadIsEmpty(DarwinCreature currentCreature){
        boolean positionAheadIsInBounds = checkPositionNearbyAndAheadIsInBounds(currentCreature);
        boolean spaceAheadIsEmpty = true;
        for(DarwinCreature otherCreature : myCreatures){
            if(otherCreature != currentCreature){
                double[] otherCreaturePosition = otherCreature.getPosition();
                if(checkOtherCreatureIsNearbyAndAhead(currentCreature, otherCreaturePosition)){
                    spaceAheadIsEmpty = false;
                }
            }
        }
        return positionAheadIsInBounds && spaceAheadIsEmpty;
    }

    private boolean checkPositionNearbyAndAheadIsInBounds(DarwinCreature currentCreature){
        double[] positionAhead = calculatePositionAhead(currentCreature, currentCreature.getSensingRange());
        boolean positionAheadIsInBounds = checkPositionIsInBounds(positionAhead);
        return positionAheadIsInBounds;
    }

    private boolean checkSameIsNearbyAndAhead(DarwinCreature currentCreature){
        for(DarwinCreature otherCreature : myCreatures){
            if(otherCreature != currentCreature && otherCreature.getSpecies().equals(currentCreature.getSpecies())){
                double[] otherCreaturePosition = otherCreature.getPosition();
                if(checkOtherCreatureIsNearbyAndAhead(currentCreature, otherCreaturePosition)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkEnemyIsNearbyAndAhead(DarwinCreature currentCreature){
        for(DarwinCreature otherCreature : myCreatures){
            if(otherCreature != currentCreature && !otherCreature.getSpecies().equals(currentCreature.getSpecies())){
                double[] otherCreaturePosition = otherCreature.getPosition();
                if(checkOtherCreatureIsNearbyAndAhead(currentCreature, otherCreaturePosition)){
                    return true;
                }
            }
        }
        return false;
    }
}
