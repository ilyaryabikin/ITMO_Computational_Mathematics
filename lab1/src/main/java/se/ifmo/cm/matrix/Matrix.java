package se.ifmo.cm.matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public abstract class Matrix {
    protected final static int DEFAULT_SCALE = 6;

    protected int rows;
    protected int cols;
    protected BigDecimal[][] array;
    protected BigDecimal determinant;

    protected Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        array = new BigDecimal[rows][];
        for (int i = 0; i < rows; i++) {
            array[i] = new BigDecimal[cols];
        }
    }

    protected Matrix(BigDecimal[][] array) {
        this.array = array;
        this.rows = array.length;
        this.cols = array[0].length;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public BigDecimal[][] getArray() {
        return array;
    }

    public BigDecimal getDeterminant() {
        if (determinant == null) {
            determinant = findDeterminant(getSquareMatrix());
        }
        return determinant;
    }

    private BigDecimal findDeterminant(BigDecimal[][] squareMatrix) {
        if (squareMatrix.length == 1) {
            return squareMatrix[0][0];
        }

        if (squareMatrix.length == 2) {
            return squareMatrix[0][0].multiply(squareMatrix[1][1])
                    .subtract(squareMatrix[0][1].multiply(squareMatrix[1][0]));
        }

        BigDecimal[][] temp;
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i < squareMatrix[0].length; i++) {
            temp = new BigDecimal[squareMatrix.length - 1][squareMatrix[0].length - 1];
            for (int j = 1; j < squareMatrix.length; j++) {
                for (int k = 0; k < squareMatrix[0].length; k++) {
                    if (k < i) {
                        temp[j - 1][k] = squareMatrix[j][k];
                    } else if (k > i) {
                        temp[j - 1][k - 1] = squareMatrix[j][k];
                    }
                }
            }
            result = result
                    .add(squareMatrix[0][i].multiply(BigDecimal.valueOf(Math.pow(-1, i)))
                            .multiply(findDeterminant(temp)));
        }
        return result.setScale(DEFAULT_SCALE, RoundingMode.UP);
    }

    private BigDecimal[][] getSquareMatrix() {
        BigDecimal[][] squareMatrix = new BigDecimal[rows][rows];
        for (int i = 0; i < rows; i++) {
            squareMatrix[i] = Arrays.copyOfRange(array[i], 0, rows);
        }
        return squareMatrix;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                builder.append(array[i][j]).append(" ");
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
