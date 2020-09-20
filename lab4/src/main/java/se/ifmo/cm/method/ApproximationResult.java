package se.ifmo.cm.method;

import org.apache.commons.math3.linear.RealVector;

import static org.apache.commons.math3.util.FastMath.pow;
import static org.apache.commons.math3.util.FastMath.sqrt;

import java.util.function.DoubleFunction;

public class ApproximationResult {
    private final DoubleFunction<Double> approximationFunction;
    private final RealVector coefficients;

    private double standardDeviation;

    public ApproximationResult(DoubleFunction<Double> approximationFunction, RealVector coefficients) {
        this.approximationFunction = approximationFunction;
        this.coefficients = coefficients;
    }

    public void setStandardDeviation(Object[][] data) {
        double deviation = 0;
        for (Object[] datum : data) {
            deviation += pow(approximationFunction.apply(Double.parseDouble(datum[0].toString()))
                    - Double.parseDouble(datum[1].toString()), 2);
        }
        this.standardDeviation = sqrt(deviation / data.length);
    }

    public DoubleFunction<Double> getApproximationFunction() {
        return approximationFunction;
    }

    @Override
    public String toString() {
        return "Coefficients: " + coefficients.toString() + "\n" +
                "Standard deviation: " + standardDeviation;
    }
}
