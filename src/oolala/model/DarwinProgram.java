package oolala.model;

import java.util.ArrayList;
import java.util.List;

public class DarwinProgram {

    private List<String> inputTokens;
    private List<String> programCommands;
    private List<Double> programArguments;

    public DarwinProgram(List<String> tokens){
        inputTokens = tokens;
        programCommands = new ArrayList<>();
        programArguments = new ArrayList<>();

        createProgram();
    }

    private void createProgram(){
        int i = 0;
        while(i < inputTokens.size()){
            programCommands.add(inputTokens.get(i).toUpperCase());
            if(Character.isDigit(inputTokens.get(i+1).charAt(0))){
                i++;
                double argument = Double.parseDouble(inputTokens.get(i));
                programArguments.add(argument);
            }
            else{
                programArguments.add(null);
            }
            i++;
        }
    }

    public String getCommand(int index){
        return programCommands.get(index);
    }

    public Double getArgument(int index){
        return programArguments.get(index);
    }

    public int getSize(){
        return programCommands.size();
    }
}
