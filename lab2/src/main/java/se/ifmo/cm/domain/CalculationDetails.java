package se.ifmo.cm.domain;

public class CalculationDetails {
    private final double result;
    private final double intervalsNumber;
    private final double calculationsError;
    private final boolean isPrecisionReached;

    public CalculationDetails(double result, double intervalsNumber,
                              double calculationsError, boolean isPrecisionReached) {
        this.result = result;
        this.intervalsNumber = intervalsNumber;
        this.calculationsError = calculationsError;
        this.isPrecisionReached = isPrecisionReached;
    }

    public double getResult() {
        return result;
    }

    public double getIntervalsNumber() {
        return intervalsNumber;
    }

    public double getCalculationsError() {
        return calculationsError;
    }

    public boolean isPrecisionReached() {
        return isPrecisionReached;
    }

    @Override
    public String toString() {
        return "Calculated result: " + result + "\n" +
                "Calculated with: " + intervalsNumber + " intervals\n" +
                "Calculations error: " + calculationsError + "\n" +
                "Desired precision " + (isPrecisionReached ? "was " : "was not ") + "reached";
    }
}
