package cucumber.runtime.model;

import gherkin.formatter.model.Step;

import java.util.List;

public interface StepContainer {

    public abstract List<Step> getSteps();

    public abstract void step(Step step);

}