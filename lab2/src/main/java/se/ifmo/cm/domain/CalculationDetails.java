package se.ifmo.cm.domain;

public class CalculationDetails {
    private final double result;
    private final double intervalsNumber;
    private final double calculationsError;

    public CalculationDetails(double result, double intervalsNumber, double calculationsError) {
        this.result = result;
        this.intervalsNumber = intervalsNumber;
        this.calculationsError = calculationsError;
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

    @Override
    public String toString() {
        return "Calculated result: " + result + "\n" +
                "Calculated with: " + intervalsNumber + " intervals\n" +
                "Calculations error: " + calculationsError;
    }
}
