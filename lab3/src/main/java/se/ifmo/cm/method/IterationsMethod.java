package se.ifmo.cm.method;

import java.util.function.DoubleFunction;

public class IterationsMethod extends CalculationMethod {

    public IterationsMethod(DoubleFunction<Double> doubleFunction) {
        super(doubleFunction);
    }

    @Override
    public CalculationSummary calculate(double a, double b, double precision) {
        if (!isRootExists(a, b)) {
            return new CalculationSummary("Root doesn't exist");
        }

        double lambda = calculateLambda(a, b);
        if (lambda * getDerivative(a) <= 0 || Math.abs(1 / lambda) < getMaxDerivative(a, b)) {
            return new CalculationSummary("Doesn't converge");
        }
        double xPrevious = a - lambda * doubleFunction.apply(a);
        double result = xPrevious;
        int iteration = 0;
        while (iteration < MAX_ITERATIONS) {
            iteration++;
            result = xPrevious - lambda * doubleFunction.apply(xPrevious);
            if (result < a || result > b) {
                return new CalculationSummary("Doesn't converge");
            }
            if (Math.abs(result - xPrevious) <= precision) {
                break;
            } else {
                xPrevious = result;
            }
        }
        boolean isPrecisionReached = iteration != MAX_ITERATIONS;
        return new CalculationSummary(result, isPrecisionReached, iteration);
    }

    private double calculateLambda(double a, double b) {
        return (1 / getMaxDerivative(a, b));
    }

    private double getMaxDerivative(double a, double b) {
        return Math.max(getDerivative(a), getDerivative(b));
    }
}
