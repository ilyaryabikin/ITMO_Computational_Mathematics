package se.ifmo.cm.function;

import java.util.function.DoubleFunction;

public enum SingleFunctions {
    FIRST(x -> Math.cos(x) * 2, "y = 2cos(x)"),
    SECOND(x -> Math.pow(x, 3) - x + 4, "y = x^3 - x + 4"),
    THIRD(x -> Math.sqrt(x + 1) - 2, "y = sqrt(x + 1) - 2");

    private final DoubleFunction<Double> doubleDoubleFunction;
    private final String stringRepresentation;

    SingleFunctions(DoubleFunction<Double> doubleDoubleFunction, String stringRepresentation) {
        this.doubleDoubleFunction = doubleDoubleFunction;
        this.stringRepresentation = stringRepresentation;
    }

    public DoubleFunction<Double> getFunction() {
        return doubleDoubleFunction;
    }

    public static String[] stringArray() {
        String[] array = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            array[i] = values()[i].stringRepresentation;
        }
        return array;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
