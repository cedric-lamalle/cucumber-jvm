package cucumber.runtime.model;

import cucumber.runtime.Runtime;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CucumberFeature {
    private final String featureUri;
    private final Feature feature;
    private CucumberBackground background;
    private StepContainer currentStepContainer;
    private CucumberScenarioOutline currentCucumberScenarioOutline;
    private List<CucumberScenario> cucumberScenarios = new ArrayList<CucumberScenario>();
    private Locale locale;

    public CucumberFeature(Feature feature, String featureUri) {
        this.feature = feature;
        this.featureUri = featureUri;
    }

    public void background(Background background) {
        this.background = new CucumberBackground(background);
        this.currentStepContainer = this.background;
    }

    public void scenario(Scenario scenario) {
        CucumberScenario newCucumberScenario = new CucumberScenario(this, featureUri, scenario);
        currentStepContainer = newCucumberScenario;
        cucumberScenarios.add(newCucumberScenario);
    }

    public void step(Step step) {
        currentStepContainer.step(step);
    }

    public Feature getFeature() {
        return feature;
    }

    public void run(Runtime runtime, Formatter formatter, Reporter reporter) {
        formatter.uri(featureUri);
        formatter.feature(feature);
        for (CucumberScenario cucumberScenario : cucumberScenarios) {
            cucumberScenario.run(runtime, formatter, reporter);
        }
    }

    public List<CucumberScenario> getCucumberScenarios() {
        return cucumberScenarios;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        this.currentCucumberScenarioOutline = new CucumberScenarioOutline(this, this.featureUri, scenarioOutline);
        this.currentStepContainer = this.currentCucumberScenarioOutline;
        cucumberScenarios.add(this.currentCucumberScenarioOutline);
    }

    public void examples(Examples examples) {
        this.currentCucumberScenarioOutline.examples(examples);
    }

    public CucumberBackground getBackground() {
        return this.background;
    }
}
