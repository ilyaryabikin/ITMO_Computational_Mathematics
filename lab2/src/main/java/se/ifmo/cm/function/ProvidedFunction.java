package se.ifmo.cm.function;

import java.util.function.DoubleFunction;

public enum ProvidedFunction {
    FIRST(x -> x + 12, "y = x + 12"),
    SECOND(x -> Math.pow(x, 2), "y = x^2"),
    THIRD(x -> Math.sqrt(x) - 4, "y = sqrt(x) - 4"),
    FOURTH(x -> Math.pow(x, 3) / Math.sqrt(Math.pow(x, 4) + 16), "y = x^3 / sqrt(x^4 + 16)"),
    FIFTH(x -> Math.sin(x), "y = sin(x)");

    private final DoubleFunction<Double> doubleDoubleFunction;
    private final String stringRepresentation;

    ProvidedFunction(DoubleFunction<Double> doubleDoubleFunction, String stringRepresentation) {
        this.doubleDoubleFunction = doubleDoubleFunction;
        this.stringRepresentation = stringRepresentation;
    }

    public DoubleFunction<Double> getFunction() {
        return doubleDoubleFunction;
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
