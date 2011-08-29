package cucumber.runtime.model;

import gherkin.formatter.Formatter;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cucumber.table.Table;

public class CucumberScenarioOutline extends CucumberScenario {

    private final List<Examples> examples =  new ArrayList<Examples>();
    private final ScenarioOutlineStepSubstitutor substitutor = new ScenarioOutlineStepSubstitutor();
    private ScenarioOutline scenarioOutline;
    
    public CucumberScenarioOutline(CucumberFeature cucumberFeature, String uri, ScenarioOutline scenarioOutline) {
        super(cucumberFeature, uri, toScenario(scenarioOutline));
        this.scenarioOutline = scenarioOutline;
    }

    private static Scenario toScenario(final ScenarioOutline scenarioOutline) {
        return new Scenario(scenarioOutline.getComments(), scenarioOutline.getTags(), scenarioOutline.getKeyword(),
                scenarioOutline.getName(), scenarioOutline.getDescription(), scenarioOutline.getLine()) {
            @Override
            public void replay(Formatter formatter) {
                formatter.scenarioOutline(scenarioOutline);
            }
        };
    }

    public List<Examples> getExamples() {
        return this.examples;
    }
    
    public void examples(Examples examples) {
        this.examples.add(examples);
    }


    private List<Step> getSubstitutedStepsFromExample(Examples example) {
        Table table = new Table(example.getRows(), getCucumberFeature().getLocale());
        List<Step> originalSteps = super.getSteps();
        List<Step> substitutedSteps = new ArrayList<Step>();
        for(Map<String, Object> exampleLine: table.hashes()) {
            substitutedSteps.addAll(this.substitutor.getSubstitutedSteps(originalSteps, exampleLine));
        }
        return substitutedSteps;
    }
    
    @Override
    public List<Step> getSteps() {
        List<Step> steps = new ArrayList<Step>();
        for (Examples example : this.examples) {
            steps.addAll(getSubstitutedStepsFromExample(example));
        }
        return steps;
    }
    
    @Override
    protected void doFormat(Formatter formatter) {
        formatter.scenarioOutline(this.scenarioOutline);
    }
    
}
