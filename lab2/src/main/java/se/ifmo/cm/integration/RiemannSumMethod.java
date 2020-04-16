package se.ifmo.cm.integration;

public abstract class RiemannSumMethod implements IntegrationMethod {
    protected static final int MAX_INTERVALS = 10000000;

   protected final int ascendingBoundsOrder;

    protected double previousSum = 0;
    protected double currentSum = Integer.MAX_VALUE;
    protected double currentIntervals = 1;

    protected double lowerBound;
    protected double upperBound;
    protected double precision;

    public RiemannSumMethod(double lowerBound, double upperBound, double precision) {
        if (lowerBound < upperBound) {
            this.ascendingBoundsOrder = 1;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        } else {
            this.ascendingBoundsOrder = -1;
            this.lowerBound = upperBound;
            this.upperBound = lowerBound;
        }
        this.precision = precision;
    }

    protected double getCurrentStep() {
        return (upperBound - lowerBound) / currentIntervals;
    }

    protected boolean isPrecisionReached() {
        return currentIntervals <= MAX_INTERVALS;
    }
}
