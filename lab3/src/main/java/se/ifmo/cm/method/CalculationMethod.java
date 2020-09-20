package se.ifmo.cm.method;

import java.util.function.DoubleFunction;

public abstract class CalculationMethod {
    protected static final double DX = 0.0001;
    protected static final int MAX_ITERATIONS = 10000;

    protected final DoubleFunction<Double> doubleFunction;

    public CalculationMethod(DoubleFunction<Double> doubleFunction) {
        this.doubleFunction = doubleFunction;
    }

    public abstract CalculationSummary calculate(double a, double b, double precision);

    protected boolean isRootExists(double a, double b) {
        return doubleFunction.apply(a) * doubleFunction.apply(b) < 0;
    }

    public boolean isRootSingle(double a, double b) {
        boolean isRootSingle = true;
        double previousValue = getDerivative(a);
        for (double i = a + DX; i < b; i += DX) {
            double currentValue = getDerivative(i);
            if (previousValue * currentValue < 0) {
                isRootSingle = false;
                break;
            }
            previousValue = currentValue;
        }
        return isRootSingle;
    }

    protected double getDerivative(double x) {
        return (doubleFunction.apply(x + DX) - doubleFunction.apply(x)) / DX;
    }
}
