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

public class CucumberScenario {
    private final List<Step> steps = new ArrayList<Step>();
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

    public List<Step> getSteps() {
        return this.steps;
    }

    public void prepare(Runtime runtime) {
        world = runtime.newWorld(tags());
        world.prepare();
    }

    public void dispose() {   
        world.dispose();
    }

    public void run(Runtime runtime, Formatter formatter, Reporter reporter) {
        for (CucumberScenario cucumberScenario : getScenariosToRun()) {
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

    public void step(Step step) {
        this.steps.add(step);
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
}
