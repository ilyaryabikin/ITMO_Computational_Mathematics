package se.ifmo.cm.method;

import java.util.function.BinaryOperator;

import static org.apache.commons.math3.util.FastMath.*;

public enum DefinedFunctions {
    FIRST((x, y) -> -(2 * y) + pow(x, 2), "x^2 - 2y"),
    SECOND((x, y) -> pow(E, x) + y, "e^x + y"),
    THIRD((x, y) -> cos(x) + y, "cos(x) + y"),
    FOURTH((x, y) -> -(2 * y + 1) * 1/tan(x), "-(2y + 1)ctg(x)");

    private final BinaryOperator<Double> function;
    private final String stringRepresentation;

    DefinedFunctions(BinaryOperator<Double> function, String stringRepresentation) {
        this.function = function;
        this.stringRepresentation = stringRepresentation;
    }

    public BinaryOperator<Double> getFunction() {
        return function;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
