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
    private final List<CucumberScenario> scenariosToRun = new ArrayList<CucumberScenario>();
    
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
        for(List<Step> steps: getSubstitutedStepsFromExamples(examples)) {
            CucumberScenario cucumberScenario = new CucumberScenario(getCucumberFeature(), getUri(), toScenario(this.scenarioOutline));
            for(Step step: steps) {
                cucumberScenario.step(step);
            }
            this.scenariosToRun.add(cucumberScenario);
        }
    }


    private List<List<Step>> getSubstitutedStepsFromExamples(Examples examples) {
        Table table = new Table(examples.getRows(), getCucumberFeature().getLocale());
        List<Step> originalSteps = getSteps();
        List<List<Step>> substitutedSteps = new ArrayList<List<Step>>();
        for(Map<String, Object> exampleLine: table.hashes()) {
            substitutedSteps.add(this.substitutor.getSubstitutedSteps(originalSteps, exampleLine));
        }
        return substitutedSteps;
    }
    
    @Override
    protected void doFormat(Formatter formatter) {
        formatter.scenarioOutline(this.scenarioOutline);
    }
    
    @Override
    public List<CucumberScenario> getScenariosToRun() {
        return this.scenariosToRun;
    }
    
}
