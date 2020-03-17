package se.ifmo.cm.elimination;

import se.ifmo.cm.exception.NoOrUnlimitedSolutionsException;
import se.ifmo.cm.matrix.Matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GaussianElimination {
    private final static int OUTPUT_SCALE = 5;

    private BigDecimal[][] equations;
    private BigDecimal[] result;
    private BigDecimal[] residuals;
    private int rows;
    private int cols;

    public GaussianElimination(Matrix linearSystem) throws NoOrUnlimitedSolutionsException {
        if (linearSystem.getDeterminant().signum() == 0) {
            throw new NoOrUnlimitedSolutionsException("This system cannot be solved because it's determinant is 0.");
        }
        this.rows = linearSystem.getRows();
        this.cols = linearSystem.getCols();
        this.equations = Arrays.copyOf(linearSystem.getArray(), rows);
        for (int i = 0; i < rows; i++) {
            if (equations[i][i].signum() == 0) {
                throw new NoOrUnlimitedSolutionsException("This system cannot be solved because one of" +
                        " the leading elements is 0.");
            }
        }
        this.result = new BigDecimal[rows];
    }

    public void solve() {
        forthTraverse();
        backTraverse();
        reverseResult();
        calculateResiduals();
    }

    public BigDecimal[] getResult() {
        if (result == null) {
            solve();
        }
        return result;
    }

    public BigDecimal[] getResiduals() {
        if (result == null) {
            solve();
        }
        return residuals;
    }

    public BigDecimal[][] getTriangleForm() {
        if (result == null) {
            solve();
        }
        return equations;
    }

    private void forthTraverse() {
        for (int n = 0; n < rows; n++) {
            BigDecimal leading = equations[n][n];
            for (int k = n; k < cols; k++) {
                equations[n][k] = equations[n][k]
                        .divide(leading, Matrix.DEFAULT_SCALE, RoundingMode.HALF_EVEN);
            }
            for (int i = n + 1; i < rows; i++) {
                BigDecimal first = equations[i][n];
                for (int j = n; j < cols; j++) {
                    equations[i][j] = equations[i][j]
                            .subtract(equations[n][j].multiply(first))
                            .setScale(Matrix.DEFAULT_SCALE, RoundingMode.HALF_EVEN);
                }
            }
        }
    }

    private void backTraverse() {
        int x = 0;
        result[x] = equations[rows - 1][cols - 1]
                .divide(equations[rows - 1][cols - 2], OUTPUT_SCALE, RoundingMode.HALF_EVEN);
        for (int i = rows - 2; i >= 0; i--) {
            BigDecimal leading = equations[i][i];
            BigDecimal tempResult = equations[i][cols - 1];
            for (int j = cols - 2, n = 0; j > i; j--, n++) {
                tempResult = tempResult
                        .subtract(equations[i][j].multiply(result[n]));
            }
            tempResult = tempResult.divide(leading, OUTPUT_SCALE, RoundingMode.HALF_EVEN);
            result[++x] = tempResult;
        }
    }

    private void reverseResult() {
        List<BigDecimal> tempList = Arrays.asList(result);
        Collections.reverse(tempList);
        result = tempList.toArray(result);
    }

    private void calculateResiduals() {
        residuals = new BigDecimal[rows];
        for (int i = 0; i < rows; i++) {
            BigDecimal leftSum = BigDecimal.ZERO;
            for (int j = 0; j < cols - 1; j++) {
                leftSum = leftSum.add(equations[i][j].multiply(result[j]));
            }
            residuals[i] = leftSum.subtract(equations[i][cols - 1]).setScale(Matrix.DEFAULT_SCALE, RoundingMode.HALF_UP);
        }
    }
}
