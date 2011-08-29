package cucumber.runtime.model;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Row;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import cucumber.runtime.World;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class CucumberScenarioOutlineTest {
    @Test
    public void shouldRunScenarioStepsForEachExample() {
        CucumberScenarioOutline cucumberScenarioOutline = new CucumberScenarioOutline(mockCucumberFeature(),
                "file:///", mock(ScenarioOutline.class));
        cucumber.runtime.Runtime mockRuntime = mock(cucumber.runtime.Runtime.class);
        World mockWorld = mock(World.class);
        when(mockRuntime.newWorld(anySet())).thenReturn(mockWorld);
        cucumberScenarioOutline.prepare(mockRuntime);
        for(Step step: cucumberEatSteps()) {
            cucumberScenarioOutline.step(step);
        }
        cucumberScenarioOutline.examples(cucumberEatExamples());
        cucumberScenarioOutline.run(mockRuntime, mock(Formatter.class), mock(Reporter.class));
        verify(mockWorld, times(6)).runStep(anyString(), any(Step.class), any(Reporter.class), any(Locale.class));
    }

    private CucumberFeature mockCucumberFeature() {
        CucumberFeature mockFeature = mock(CucumberFeature.class);
        when(mockFeature.getFeature()).thenReturn(mock(Feature.class));
        return mockFeature;
    }

    protected Examples cucumberEatExamples() {
        Examples examples = mock(Examples.class);
        List<Row> rows = new ArrayList<Row>();
        ArrayList<Comment> noComments = new ArrayList<Comment>();
        rows.add(new Row(noComments, Arrays.asList("start", "eat", "left" ), 1));
        rows.add(new Row(noComments, Arrays.asList("12", "5",  "7"), 1));
        rows.add(new Row(noComments, Arrays.asList("20", "5", "15" ), 1));
        when(examples.getRows()).thenReturn(rows);
        return examples;
    }

    protected List<Step> cucumberEatSteps() {
        List<Step> steps = new ArrayList<Step>();
        steps.add(new Step(new ArrayList<Comment>(), "Given", "there are <start> cucumbers", 1));
        steps.add(new Step(new ArrayList<Comment>(), "When", "I eat <eat> cucumbers", 2));
        steps.add(new Step(new ArrayList<Comment>(), "Then", "I should have <left> cucumbers", 3));
        return steps;
    }
}
