package se.ifmo.cm.method;

public enum ProvidedMethods {
    BISECTION("Bisection method"),
    ITERATIONS("Iterations method");

    private final String stringRepresentation;

    ProvidedMethods(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
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
