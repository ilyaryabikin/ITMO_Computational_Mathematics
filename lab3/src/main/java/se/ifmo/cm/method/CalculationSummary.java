package se.ifmo.cm.method;

public class CalculationSummary {

    private final boolean isCalculated;
    private final double calculatedValue;
    private final boolean isPrecisionReached;
    private final int iterations;
    private final boolean isNumericValue;
    private final String failMessage;

    public CalculationSummary(String failMessage) {
        this.failMessage = failMessage;
        this.isCalculated = false;
        this.calculatedValue = Double.NaN;
        this.iterations = -1;
        this.isPrecisionReached = false;
        this.isNumericValue = false;
    }

    public CalculationSummary(double calculatedValue, boolean isPrecisionReached, int iterations) {
        this.isCalculated = true;
        this.calculatedValue = calculatedValue;
        this.isPrecisionReached = isPrecisionReached;
        this.iterations = iterations;
        this.isNumericValue = Double.isFinite(calculatedValue);
        this.failMessage = "";
    }

    public boolean isCalculated() {
        return isCalculated;
    }

    public double getCalculatedValue() {
        return calculatedValue;
    }

    public boolean isPrecisionReached() {
        return isPrecisionReached;
    }

    public int getIterations() {
        return iterations;
    }

    public boolean isNumericValue() {
        return isNumericValue;
    }

    public String getFailMessage() {
        return failMessage;
    }
}
