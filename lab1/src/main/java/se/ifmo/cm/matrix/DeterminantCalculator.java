package se.ifmo.cm.matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;

class DeterminantCalculator {
    private static final int CALCULATION_SCALE = 100;

    private BigDecimal[][] matrix;
    private int sign = 1;

    protected DeterminantCalculator(BigDecimal[][] matrix) {
        this.matrix = matrix;
    }

    protected BigDecimal calculateDeterminant() {
        BigDecimal determinant;
        if (!isUpperTriangular() && !isLowerTriangular()) {
            makeTriangular();
        }
        determinant = multiplyDiameter().multiply(BigDecimal.valueOf(sign));
        return determinant.setScale(Matrix.DEFAULT_SCALE, RoundingMode.HALF_EVEN);
    }

    private boolean isUpperTriangular() {
        if (matrix.length < 2) {
            return false;
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < i; j++) {
                if (matrix[i][j].signum() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isLowerTriangular() {
        if (matrix.length < 2) {
            return false;
        }
        for (int j = 0; j < matrix.length; j++) {
            for (int i = 0; j > i; i++) {
                if (matrix[i][j].signum() != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private BigDecimal multiplyDiameter() {
        BigDecimal result = BigDecimal.ONE;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i == j) {
                    result = result.multiply(matrix[i][j]);
                }
            }
        }
        return result;
    }

    private void makeTriangular() {
        for (int j = 0; j < matrix.length; j++) {
            sortColumn(j);
            for (int i = matrix.length - 1; i > j; i--) {
                if (matrix[i][j].signum() == 0) {
                    continue;
                }
                BigDecimal first = matrix[i][j];
                BigDecimal second = matrix[i - 1][j];
                multiplyRow(i, second.negate().divide(first, CALCULATION_SCALE, RoundingMode.HALF_EVEN));
                addRow(i, i - 1);
                multiplyRow(i, first.negate().divide(second, CALCULATION_SCALE, RoundingMode.HALF_EVEN));
            }
        }
    }

    private void sortColumn(int column) {
        for (int i = matrix.length - 1; i >= column; i--) {
            for (int j = matrix.length - 1; j >= column; j--) {
                BigDecimal first = matrix[i][column];
                BigDecimal second = matrix[j][column];

                if (first.abs().compareTo(second.abs()) < 0) {
                    replaceRow(i, j);
                }
            }
        }
    }

    private void replaceRow(int firstRow, int secondRow) {
        if (firstRow != secondRow) {
            sign *= -1;
        }
        BigDecimal[] tempRow = new BigDecimal[matrix.length];
        for (int j = 0; j < matrix.length; j++) {
            tempRow[j] = matrix[firstRow][j];
            matrix[firstRow][j] = matrix[secondRow][j];
            matrix[secondRow][j] = tempRow[j];
        }
    }

    private void addRow(int firstRow, int secondRow) {
        for (int j = 0; j < matrix.length; j++) {
            matrix[firstRow][j] = matrix[firstRow][j].add(matrix[secondRow][j]);
        }
    }

    private void multiplyRow(int row, BigDecimal value) {
        if (value.signum() < 0) {
            sign *= -1;
        }
        for (int j = 0; j < matrix.length; j++) {
            matrix[row][j] = matrix[row][j].multiply(value);
        }
    }
}
