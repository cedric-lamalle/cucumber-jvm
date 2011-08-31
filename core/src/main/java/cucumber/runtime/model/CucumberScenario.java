package cucumber.runtime.model;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cucumber.runtime.Runtime;
import cucumber.runtime.World;
import gherkin.formatter.model.Tag;

public class CucumberScenario extends BaseStepContainer {
    private final Scenario scenario;
    private final CucumberFeature cucumberFeature;
    private final String uri;

    private World world;

    public CucumberScenario(CucumberFeature cucumberFeature, String uri, Scenario scenario) {
        this.cucumberFeature = cucumberFeature;
        this.uri = uri;
        this.scenario = scenario;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void prepare(Runtime runtime) {
        world = runtime.newWorld(tags());
        world.prepare();
    }

    public void dispose() {   
        world.dispose();
    }

    public void run(Runtime runtime, Formatter formatter, Reporter reporter) {
        for (StepContainer cucumberScenario : getScenariosToRun()) {
            prepare(runtime);
            doFormat(formatter);
            for (Step step : cucumberScenario.getSteps()) {
                formatter.step(step);
            }
            for (Step step : cucumberScenario.getSteps()) {
                runStep(step, reporter);
            }
            dispose();
        }
    }

    protected void doFormat(Formatter formatter) {
        formatter.scenario(scenario);
    }

    public void runStep(Step step, Reporter reporter) {
        world.runStep(uri, step, reporter, cucumberFeature.getLocale());
    }

    private Set<String> tags() {
        Set<String> tags = new HashSet<String>();
        for (Tag tag : cucumberFeature.getFeature().getTags()) {
            tags.add(tag.getName());
        }
        for (Tag tag : scenario.getTags()) {
            tags.add(tag.getName());
        }
        return tags;
    }

    public CucumberFeature getCucumberFeature() {
        return this.cucumberFeature;
    }
    
    public List<CucumberScenario> getScenariosToRun() {
        return Arrays.asList(this);
    }

    public String getUri() {
        return this.uri;
    }
    
    @Override
    public List<Step> getSteps() {
        List<Step> allSteps;
        CucumberBackground background = getCucumberFeature().getBackground();
        if (background != null) {
            allSteps = new ArrayList<Step>();
            allSteps.addAll(background.getSteps());
            allSteps.addAll(super.getSteps());
        } else {
            allSteps = super.getSteps();
        }
        return allSteps;
    }
}
