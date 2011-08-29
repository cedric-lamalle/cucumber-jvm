package cucumber.runtime.model;

import gherkin.formatter.Argument;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cucumber.runtime.CucumberException;

/**
 * 
 * Class responsible to substitute placeholders in Steps
 * 
 */
public class ScenarioOutlineStepSubstitutor {

    public List<Step> getSubstitutedSteps(List<Step> originalSteps, Map<String, Object> exampleLine) {
        List<Step> substitutedSteps = new ArrayList<Step>();
        for (Step originalStep : originalSteps) {
            substitutedSteps.add(getSubstitutedStep(originalStep, exampleLine));
        }
        return substitutedSteps;
    }

    protected Step getSubstitutedStep(Step originalStep, Map<String, Object> exampleLine) {
        if (!originalStep.getOutlineArgs().isEmpty()) {
            String newName = substitutePlaceHolders(originalStep, exampleLine);
            return new Step(originalStep.getComments(), originalStep.getKeyword(), newName, originalStep.getLine());
        } else {
            return originalStep;
        }
    }

    public String substitutePlaceHolders(Step originalStep, Map<String, Object> exampleLine) {
        String newName = originalStep.getName();
        for (Argument placeholder : originalStep.getOutlineArgs()) {
            Object value = exampleLine.get(placeholder.getVal().substring(1, placeholder.getVal().length() - 1));
            if (value != null) {
                newName = newName.replaceAll(placeholder.getVal(), value.toString());
            } else {
                throw new CucumberException("Couldn't find placeholder substitute for " + placeholder.getVal()
                        + " on line " + originalStep.getLine() + " in example table");
            }
        }
        return newName;
    }
}
