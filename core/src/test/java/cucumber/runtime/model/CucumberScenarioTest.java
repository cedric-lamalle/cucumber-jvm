package cucumber.runtime.model;

import gherkin.formatter.model.Background;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CucumberScenarioTest {
    @Test
    public void shouldReturnBackgroundAndScenarioSteps() {
        CucumberFeature feature = mock(CucumberFeature.class);
        CucumberBackground background = new CucumberBackground(mock(Background.class));
        background.step(mock(Step.class));
        when(feature.getBackground()).thenReturn(background);
        CucumberScenario cucumberScenario = new CucumberScenario(feature, "test-uri", mock(Scenario.class));
        cucumberScenario.step(mock(Step.class));
        assertEquals("Number of steps returned by scenario",2, cucumberScenario.getSteps().size());
    }
}
