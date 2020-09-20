package se.ifmo.cm.method;

import java.util.function.DoubleFunction;

public class BisectionMethod extends CalculationMethod {

    public BisectionMethod(DoubleFunction<Double> doubleFunction) {
        super(doubleFunction);
    }

    @Override
    public CalculationSummary calculate(double a, double b, double precision) {
        if (!isRootExists(a, b)) {
            return new CalculationSummary("Root doesn't exist");
        }

        int iteration = 0;
        double x = (a + b) / 2;
        while (iteration < MAX_ITERATIONS) {
            if (isDifferentSigns(a, x)) {
                b = x;
            } else {
                a = x;
            }
            x = (a + b) / 2;
            double result = doubleFunction.apply(x);
            if (Math.abs(b - a) <= precision || Math.abs(result) <= precision) {
                break;
            }
            iteration++;
        }
        boolean isPrecisionReached = iteration != MAX_ITERATIONS;
        return new CalculationSummary(x, isPrecisionReached, iteration);
    }

    private boolean isDifferentSigns(double a, double b) {
        return doubleFunction.apply(a) * doubleFunction.apply(b) < 0;
    }
}
