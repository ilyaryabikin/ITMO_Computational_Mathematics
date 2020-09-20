package se.ifmo.cm.method;

import org.apache.commons.math3.linear.*;

import java.util.List;
import java.util.function.DoubleFunction;

import static org.apache.commons.math3.util.FastMath.*;

public class LeastSquaresApproximation {
    private final List<Point> data;
    private final int dataSize;
    private final double[] xData;
    private final double[] yData;
    private final ApproximationFunction approximationFunction;

    private RealMatrix coefficientsMatrix;
    private RealVector constantsVector;

    public LeastSquaresApproximation(List<Point> data, ApproximationFunction approximationFunction) {
        this.data = data;
        this.dataSize = data.size();
        this.xData = new double[dataSize];
        this.yData = new double[dataSize];
        this.approximationFunction = approximationFunction;

        for (int i = 0; i < dataSize; i++) {
            xData[i] = data.get(i).getX();
            yData[i] = data.get(i).getY();
        }
    }

    public DoubleFunction<Double> getResult() {
        RealVector coefficients;
        DoubleFunction<Double> resultFunction = x -> x;
        switch (approximationFunction) {
            case LINEAR:
                calculateLinear();
                coefficients = solveSystem();
                resultFunction = x -> coefficients.getEntry(0) * x + coefficients.getEntry(1);
                break;
            case SQUARED:
                calculateSquare();
                coefficients = solveSystem();
                resultFunction = x -> coefficients.getEntry(0) + coefficients.getEntry(1) * x +
                        coefficients.getEntry(2) * x * x;
                break;
            case POWERED:
                for (int i = 0; i < dataSize; i++) {
                    xData[i] = log(xData[i]);
                    yData[i] = log(yData[i]);
                }
                calculateLinear();
                coefficients = solveSystem();
                resultFunction = x -> pow(E, coefficients.getEntry(0)) *
                        pow(x, coefficients.getEntry(1));
                break;
            case EXPONENTIAL:
                for (int i = 0; i < dataSize; i++) {
                    yData[i] = log(yData[i]);
                }
                calculateLinear();
                coefficients = solveSystem();
                resultFunction = x -> pow(E, coefficients.getEntry(1)) *
                        pow(E, x * coefficients.getEntry(0));
                break;
            case LOGARITHMIC:
                for (int i = 0; i < dataSize; i++) {
                    xData[i] = log(xData[i]);
                }
                calculateLinear();
                coefficients = solveSystem();
                resultFunction = x -> coefficients.getEntry(0) * log(x) + coefficients.getEntry(1);
                break;
            default:
                coefficients = new ArrayRealVector(0);
        }
        return resultFunction;
    }

    private void calculateSquare() {
        double sumX = 0;
        double sumX2 = 0;
        double sumX3 = 0;
        double sumX4 = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2Y = 0;
        for (int i = 0; i < dataSize; i++) {
            sumX += xData[i];
            sumX2 += pow(xData[i], 2);
            sumX3 += pow(xData[i], 3);
            sumX4 += pow(xData[i], 4);
            sumY += yData[i];
            sumXY += xData[i] * yData[i];
            sumX2Y += pow(xData[i], 2) * yData[i];
        }

        this.coefficientsMatrix = new Array2DRowRealMatrix(new double[][] {
                {dataSize, sumX, sumX2}, {sumX, sumX2, sumX3}, {sumX2, sumX3, sumX4}
        }, false);
        this.constantsVector = new ArrayRealVector(new double[] {sumY, sumXY, sumX2Y}, false);
    }

    private void calculateLinear() {
        double sumX = 0;
        double sumXX = 0;
        double sumY = 0;
        double sumXY = 0;
        for (int i = 0; i < dataSize; i++) {
            sumX += xData[i];
            sumY += yData[i];
            sumXX += xData[i] * xData[i];
            sumXY += xData[i] * yData[i];
        }

        this.coefficientsMatrix = new Array2DRowRealMatrix(new double[][] {
                {sumXX, sumX}, {sumX, dataSize}
        }, false);
        this.constantsVector = new ArrayRealVector(new double[] {sumXY, sumY}, false);
    }

    private RealVector solveSystem() {
        DecompositionSolver solver = new QRDecomposition(coefficientsMatrix).getSolver();
        return solver.solve(constantsVector);
    }
}
