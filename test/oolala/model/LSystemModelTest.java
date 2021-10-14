package oolala.model;

import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LSystemModelTest  extends DukeApplicationTest {

    private final String FILE_PATH = "data/examples/lsystem/test.txt";
    private final String SNOW_FLAKE = "data/examples/lsystem/koch_snowflake.txt";
    private final String SNOW_FLAKE_OUTPUT = "F-F++F-F-F-F++F-F++F-F++F-F-F-F++F-F-F-F++F-F-F-F++F-F++F-F++F-F-F-F++F-F++F-F++F-F-F-F++F-F++F-F++F-F-F-F++F-F-F-F++F-F-F-F++F-F++F-F++F-F-F-F++F-F".toLowerCase();

    @Test
    public void testSnowFlakeString() throws FileNotFoundException {
        int numOfLevels = 3;
        LSystemModel model = new LSystemModel(numOfLevels, 10, 30);
        model.updateKeys(SNOW_FLAKE);

        // test extracted key tokens
        List<String> LSystemKeyTokens = model.getLSystemCommandList();
        assertTrue(LSystemKeyTokens.get(0).equals("start"));
        assertTrue(LSystemKeyTokens.get(2).equals("rule"));
        assertTrue(LSystemKeyTokens.get(4).equals("f-f++f-f"));
    }

    @Test
    public void testRules() throws FileNotFoundException {
        int numOfLevels = 3;
        LSystemModel model = new LSystemModel(numOfLevels, 10, 30);
        model.updateKeys(SNOW_FLAKE);

        // test extracted key tokens
        model.keyTranslation();
        Map<String, String> rule = model.getMyRules();
        assertTrue(rule.get("F".toLowerCase()).equals("F-F++F-F".toLowerCase()));
    }

    @Test
    public void testFinalLayerString() throws FileNotFoundException {
        int numOfLevels = 3;
        LSystemModel model = new LSystemModel(numOfLevels, 10, 30);
        model.updateKeys(SNOW_FLAKE);
        model.keyTranslation();
        model.buildExpansion(model.getMyRules());
        Map<Integer, String> modelExpansion = model.getExpandedSymbols();
        assertTrue(modelExpansion.get(3).equals(SNOW_FLAKE_OUTPUT.toLowerCase()));
    }

    @Test
    public void testWriteIntoTokens() throws FileNotFoundException {
        int numOfLevels = 3;
        LSystemModel model = new LSystemModel(numOfLevels, 10, 30);
        model.updateKeys(SNOW_FLAKE);
        model.keyTranslation();
        model.buildExpansion(model.getMyRules());
        model.writeTokenList();

        assertTrue(model.getLogoModelTokens().get(0).equals("tell"));
        assertTrue(model.getLogoModelTokens().get(1).equals("1"));
        assertTrue(model.getLogoModelTokens().get(2).equals("LSystemTurtle"));
        assertTrue(model.getLogoModelTokens().get(3).equals("ht"));
        assertTrue(model.getLogoModelTokens().get(5).equals("fd"));
        assertTrue(model.getLogoModelTokens().get(8).equals(Double.toString(30)));
        assertTrue(model.getLogoModelTokens().get(18).equals(Double.toString(10)));
    }

    @Test
    public void testSetLogoCommands() throws FileNotFoundException {
        int numOfLevels = 3;
        LSystemModel model = new LSystemModel(numOfLevels, 10, 30);
        model.updateKeys(FILE_PATH);
        model.keyTranslation();
        Map<String, String> rule = model.getMyRules();

        // test if new rules are being added
        assertTrue(rule.get("F".toLowerCase()).equals("F-F++F-F".toLowerCase()));
        assertTrue(rule.get("G".toLowerCase()).equals("G+-g".toLowerCase()));

        // test if new maps between keys and Logo commands are set up properly
        Map<String, List<String>> LSystemCommandMap = model.getLSystemCommandMap();
        assertTrue(LSystemCommandMap.get("U".toLowerCase()).equals(Arrays.asList("pu", "fd", "100")));
        assertTrue(LSystemCommandMap.get("T".toLowerCase()).equals(Arrays.asList("pd", "fd", "100")));
    }
}
