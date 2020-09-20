package se.ifmo.cm.method;

import se.ifmo.cm.exception.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

public class EulerMethod {
    private final BinaryOperator<Double> function;
    private final double x0;
    private final double y0;
    private final double n;
    private final double h;

    public EulerMethod(BinaryOperator<Double> function, double x0, double y0, double n, double h) throws InvalidArgumentException {
        if (x0 >= n) {
            throw new InvalidArgumentException("Left border must be less than right border");
        }
        if (h <= 0) {
            throw new InvalidArgumentException("Precision must be less or greater than zero");
        }
        this.function = function;
        this.x0 = x0;
        this.y0 = y0;
        this.n = n;
        this.h = h;
    }

    public List<Point> calculate() throws InvalidArgumentException {
        List<Point> resultArray = new ArrayList<>();
        resultArray.add(new Point(x0, y0));
        double xPrev = x0;
        double yPrev = y0;
        while (xPrev < n) {
            double x = xPrev + h;
            double y = yPrev + h * function.apply(xPrev, yPrev);
            if (!Double.isFinite(y)) {
                throw new InvalidArgumentException("Cannot calculate f(" + xPrev + "," + yPrev + "). Not finite number");
            }
            resultArray.add(new Point(x, y));
            xPrev = x;
            yPrev = y;
        }
        return resultArray;
    }

    public BinaryOperator<Double> getFunction() {
        return function;
    }
}
