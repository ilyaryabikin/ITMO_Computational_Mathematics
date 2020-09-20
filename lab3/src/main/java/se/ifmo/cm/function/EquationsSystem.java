package se.ifmo.cm.function;

import java.util.function.BiFunction;
import java.util.function.DoubleFunction;

public enum EquationsSystem {
    FIRST((x,y) -> Math.sin(y - 1) + x - 1.3, y -> 1.3 - Math.sin(y - 1), "sin(y - 1) + x = 1.3"),
    SECOND((x, y) -> y - Math.sin(x + 1) - 0.8, x -> 0.8 + Math.sin(x + 1), "y - sin(x + 1) = 0.8");

    private final BiFunction<Double, Double, Double> biFunction;
    private final DoubleFunction<Double> chartFunction;
    private final String stringRepresentation;

    EquationsSystem(BiFunction<Double, Double, Double> biFunction, DoubleFunction<Double> chartFunction, String stringRepresentation) {
        this.biFunction = biFunction;
        this.chartFunction = chartFunction;
        this.stringRepresentation = stringRepresentation;
    }

    public static double[][] getJacobi(double[] xVector) {
        double[][] jacobi = new double[2][2];
        jacobi[0][0] = 1;
        jacobi[0][1] = Math.cos(xVector[1] - 1);
        jacobi[1][0] = -Math.cos(xVector[0] + 1);
        jacobi[1][1] = 1;
        return jacobi;
    }

    public static double[] functions(double[] xVector) {
        double[] function = new double[2];
        function[0] = Math.sin(xVector[1] - 1) + xVector[0] - 1.3;
        function[1] = xVector[1] - Math.sin(xVector[0] + 1) - 0.8;
        return function;
    }

    public DoubleFunction<Double> getChartFunction() {
        return chartFunction;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
