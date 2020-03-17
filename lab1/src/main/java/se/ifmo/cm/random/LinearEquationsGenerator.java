package se.ifmo.cm.random;

import se.ifmo.cm.matrix.LinearEquationsSystem;
import se.ifmo.cm.matrix.Matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class LinearEquationsGenerator {
    private final static int GENERATION_SCALE = 10;
    private final static int SOLUTION_UPPER_BOUND = 5000000;

    private static int variables;

    public static Matrix generateLinearSystem(int size) {
        variables = size;
        BigDecimal[] solutions = generateSolution();
        BigDecimal[][] leftPart = generateLeftPart(solutions);
        BigDecimal[] rightPart = generateRightPart(leftPart, solutions);
        BigDecimal[][] result = new BigDecimal[variables][variables + 1];
        for (int i = 0; i < variables; i++) {
            for (int j = 0; j < variables; j++) {
                result[i][j] = leftPart[i][j].setScale(Matrix.DEFAULT_SCALE, RoundingMode.UP);
            }
            result[i][variables] = rightPart[i].setScale(Matrix.DEFAULT_SCALE, RoundingMode.UP);
        }
        return new LinearEquationsSystem(result);
    }

    private static BigDecimal[] generateSolution() {
        BigDecimal[] solutions = new BigDecimal[variables];
        Random random = new Random();
        for (int i = 0; i < variables; i++) {
            int n = random.nextInt(SOLUTION_UPPER_BOUND);
            double m = 2 * random.nextGaussian() - 1;
            solutions[i] = BigDecimal.valueOf(n * m).setScale(GENERATION_SCALE, RoundingMode.UP);
        }
        return solutions;
    }

    private static BigDecimal[][] generateLeftPart(BigDecimal[] solutions) {
        BigDecimal[][] leftPart = new BigDecimal[variables][variables];
        Random random = new Random();
        for (int i = 0; i < variables; i++) {
            for (int j = 0; j < variables; j++) {
                int n = random.nextInt();
                double m = 2 * random.nextGaussian() - 1;
                BigDecimal generatedCoefficient = BigDecimal.valueOf(n * m)
                        .divide(solutions[j], GENERATION_SCALE, RoundingMode.UP);
                leftPart[i][j] = generatedCoefficient;
            }
        }
        return leftPart;
    }

    private static BigDecimal[] generateRightPart(BigDecimal[][] leftPart, BigDecimal[] solutions) {
        BigDecimal[] rightPart = new BigDecimal[variables];
        for (int i = 0; i < variables; i++) {
            BigDecimal currentSum = BigDecimal.ZERO;
            for (int j = 0; j < variables; j++) {
                currentSum = currentSum.add(leftPart[i][j].multiply(solutions[j]));
            }
            rightPart[i] = currentSum;
        }
        return rightPart;
    }
}
