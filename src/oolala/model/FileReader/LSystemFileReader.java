package oolala.model.FileReader;

import oolala.model.FileReader.FileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LSystemFileReader extends FileReader {

    public LSystemFileReader(String fileName) {
        super(fileName);
    }

    @Override
    protected void extractTokens(List<String> commands) {

        for (int i = 0; i < commands.size(); i++) {
//            String[] splitCommands = commands.get(i).split(" ");
            String[] splitCommands = splitCommandPhrase(commands.get(i)).toArray(new String[0]);
            splitCommands = trimArray(splitCommands);

            tokens.addAll(Arrays.asList(splitCommands));
        }

        // clean up commands again
        processCommands(tokens);
    }

    private List<String> splitCommandPhrase(String targetString) {
        List<String> matchList = new ArrayList<>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
        Matcher regexMatcher = regex.matcher(targetString);
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null) {
                // Add double-quoted string without the quotes
                matchList.add(regexMatcher.group(1));
            } else if (regexMatcher.group(2) != null) {
                // Add single-quoted string without the quotes
                matchList.add(regexMatcher.group(2));
            } else {
                // Add unquoted word
                matchList.add(regexMatcher.group());
            }
        }
        return matchList;
    }
}
