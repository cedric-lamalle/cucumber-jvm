package cucumber.runtime.model;

import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseStepContainer implements StepContainer {

    private final List<Step> steps = new ArrayList<Step>();

    public BaseStepContainer() {
        super();
    }

    @Override
    public List<Step> getSteps() {
        return this.steps;
    }

    @Override
    public void step(Step step) {
        this.steps.add(step);
    }

}