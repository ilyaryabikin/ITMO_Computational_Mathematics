package se.ifmo.cm.method;

public class NewtonCalculationSummary {
    private final double[] calculatedVector;
    private final boolean isPrecisionReached;
    private final int iterationsNumber;

    public NewtonCalculationSummary(double[] calculatedVector, boolean isPrecisionReached, int iterationsNumber) {
        this.calculatedVector = calculatedVector;
        this.isPrecisionReached = isPrecisionReached;
        this.iterationsNumber = iterationsNumber;
    }

    public double[] getCalculatedVector() {
        return calculatedVector;
    }

    public boolean isPrecisionReached() {
        return isPrecisionReached;
    }

    public int getIterationsNumber() {
        return iterationsNumber;
    }
}
