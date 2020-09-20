package se.ifmo.cm.method;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import se.ifmo.cm.function.EquationsSystem;

public class NewtonMethod {
    protected static final int MAX_ITERATIONS = 10000;

    public NewtonMethod() {}

    public NewtonCalculationSummary calculate(double x0, double y0, double precision) {
        double[] xVector = new double[2];
        xVector[0] = x0;
        xVector[1] = y0;
        RealVector xInit = new ArrayRealVector(xVector);
        RealVector xk;
        int iteration = 0;
        while (iteration < MAX_ITERATIONS) {
            RealMatrix w = MatrixUtils.createRealMatrix(EquationsSystem.getJacobi(xInit.toArray()));
            w = w.transpose();
            RealVector f = new ArrayRealVector(EquationsSystem.functions(xInit.toArray()));
            RealVector m = w.operate(f);
            xk = xInit.subtract(m);
            if (Math.abs(xk.getNorm() - xInit.getNorm()) <= precision) {
                break;
            } else {
                xInit = xk;
                iteration++;
            }
        }
        boolean isPrecisionReached = iteration != MAX_ITERATIONS;
        return new NewtonCalculationSummary(xInit.toArray(), isPrecisionReached, iteration);
    }
}
