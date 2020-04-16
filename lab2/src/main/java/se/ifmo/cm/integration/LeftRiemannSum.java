package se.ifmo.cm.integration;

import se.ifmo.cm.domain.CalculationDetails;

import java.util.function.DoubleFunction;

public class LeftRiemannSum extends RiemannSumMethod {

    public LeftRiemannSum(double lowerBound, double upperBound, double precision) {
        super(lowerBound, upperBound, precision);
    }

    @Override
    public CalculationDetails integrate(DoubleFunction<Double> function) {
        if (lowerBound != upperBound) {
            for (currentIntervals = 1; (Math.abs(currentSum - previousSum) > precision) &&
                    currentIntervals < MAX_INTERVALS; currentIntervals *= 2) {
                previousSum = currentSum;
                currentSum = 0;
                double step = getCurrentStep();
                for (double x = lowerBound; x < upperBound; x += step) {
                    currentSum += function.apply(x);
                }
                currentSum *= step * ascendingBoundsOrder;
            }
        } else {
            currentSum = 0;
        }
        double calculationsError = Math.abs(currentSum - previousSum);
        return new CalculationDetails(currentSum, currentIntervals,
                calculationsError, isPrecisionReached());
    }
}
