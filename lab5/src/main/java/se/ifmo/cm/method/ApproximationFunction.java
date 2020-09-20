package se.ifmo.cm.method;

public enum  ApproximationFunction {
    LINEAR("Linear"),
    SQUARED("Squared"),
    POWERED("Powered"),
    EXPONENTIAL("Exponential"),
    LOGARITHMIC("Logarithmic");

    private final String value;

    ApproximationFunction(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
