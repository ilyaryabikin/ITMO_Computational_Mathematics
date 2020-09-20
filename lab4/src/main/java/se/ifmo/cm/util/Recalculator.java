package se.ifmo.cm.util;

import java.util.function.DoubleFunction;

import static org.apache.commons.math3.util.FastMath.abs;

public class Recalculator {
    private final Object[][] data;
    private final DoubleFunction<Double> function;

    private int maxDeviationIndex;

    public Recalculator(Object[][] data, DoubleFunction<Double> function) {
        this.data = data;
        this.function = function;
    }

    public Object[][] recalculate() {
        int index = calculateIndexWithMaxDeviation();
        this.maxDeviationIndex = index;
        Object[][] newData = new Object[data.length - 1][2];

        for (int i = 0, j = 0; i < data.length; i++) {
            if (i == index) {
                continue;
            }
            newData[j++] = data[i];
        }
        return newData;
    }

    public int getMaxDeviationIndex() {
        return maxDeviationIndex;
    }

    private int calculateIndexWithMaxDeviation() {
        int pointIndex = 0;
        double maxDeviation = Double.MIN_VALUE;

        for (int i = 0; i < data.length; i++) {
            double currentValue = function.apply(Double.parseDouble(data[i][0].toString()));
            double currentDeviation = abs(currentValue - Double.parseDouble(data[i][1].toString()));
            if (currentDeviation > maxDeviation) {
                maxDeviation = currentDeviation;
                pointIndex = i;
            }
        }
        return pointIndex;
    }
}
