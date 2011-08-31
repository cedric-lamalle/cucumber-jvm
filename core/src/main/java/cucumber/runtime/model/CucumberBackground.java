package cucumber.runtime.model;

import gherkin.formatter.model.Background;

public class CucumberBackground extends BaseStepContainer {
    private Background background;

    public CucumberBackground(Background background) {
        this.background = background;
    }
}
