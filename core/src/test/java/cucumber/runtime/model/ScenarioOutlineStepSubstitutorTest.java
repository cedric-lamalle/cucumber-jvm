package cucumber.runtime.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gherkin.formatter.model.Comment;
import gherkin.formatter.model.Step;

import org.junit.Test;

import cucumber.runtime.CucumberException;
import static org.junit.Assert.*;

public class ScenarioOutlineStepSubstitutorTest {
    @Test
    public void shouldSubstitutePlaceHolders() {
        Step originalStep = new Step(new ArrayList<Comment>(), "Given", "I have <bananas> bananas and <apples> apples", 1);
        Map<String, Object> exampleLine = new HashMap<String, Object>();
        exampleLine.put("bananas", "5");
        exampleLine.put("apples", "4");
        String substitutePlaceHolders = new ScenarioOutlineStepSubstitutor().substitutePlaceHolders(originalStep, exampleLine);
        assertEquals("I have 5 bananas and 4 apples", substitutePlaceHolders);
    }
    
    @Test
    public void shouldSubstitutePlaceHoldersWithSpaces() {
        Step originalStep = new Step(new ArrayList<Comment>(), "Given", "I have <number of bananas> bananas and <number of apples> apples", 1);
        Map<String, Object> exampleLine = new HashMap<String, Object>();
        exampleLine.put("number of bananas", "5");
        exampleLine.put("number of apples", "4");
        String substitutePlaceHolders = new ScenarioOutlineStepSubstitutor().substitutePlaceHolders(originalStep, exampleLine);
        assertEquals("I have 5 bananas and 4 apples", substitutePlaceHolders);
    }
    
    @Test(expected=CucumberException.class)
    public void shouldThrowExceptionIfNoSubstitutes() {
        Step originalStep = new Step(new ArrayList<Comment>(), "Given", "I have <bananas> bananas and <apples> apples", 1);
        new ScenarioOutlineStepSubstitutor().substitutePlaceHolders(originalStep, new HashMap<String, Object>());
    }
    
    @Test
    public void shouldSubstituteSteps() {
        List<Step> steps = new ArrayList<Step>();
        steps.add(new Step(new ArrayList<Comment>(), "Given", "there are <start> cucumbers", 1));
        steps.add(new Step(new ArrayList<Comment>(), "When", "I eat <eat> cucumbers", 2));
        steps.add(new Step(new ArrayList<Comment>(), "Then", "I should have <left> cucumbers", 3));
        Map<String, Object> exampleLine = new HashMap<String, Object>();
        exampleLine.put("start", "12");
        exampleLine.put("eat", "5");
        exampleLine.put("left", "7");
        List<Step> substitutedSteps = new ScenarioOutlineStepSubstitutor().getSubstitutedSteps(steps, exampleLine);
        assertEquals("Number of substituted tests", 3, substitutedSteps.size());
        assertEquals("Given Step", "there are 12 cucumbers", substitutedSteps.get(0).getName());
        assertEquals("When Step", "I eat 5 cucumbers", substitutedSteps.get(1).getName());
        assertEquals("Then Step", "I should have 7 cucumbers", substitutedSteps.get(2).getName());
    }
}
