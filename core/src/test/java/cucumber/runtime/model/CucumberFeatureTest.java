package cucumber.runtime.model;

import gherkin.formatter.model.Background;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Step;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CucumberFeatureTest {
    @Test
    public void shouldAddTwoStepToBackground() {
        CucumberFeature cucumberFeature = new CucumberFeature(mock(Feature.class), "test-uri");
        cucumberFeature.background(mock(Background.class));
        cucumberFeature.step(mock(Step.class));
        cucumberFeature.step(mock(Step.class));
        assertEquals("Number of steps", 2, cucumberFeature.getBackground().getSteps().size());
    }
}
