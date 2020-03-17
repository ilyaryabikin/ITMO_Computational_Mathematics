package se.ifmo.cm.matrix;

import java.math.BigDecimal;
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
            DeterminantCalculator determinantCalculator = new DeterminantCalculator(getSquareMatrix());
            determinant = determinantCalculator.calculateDeterminant();
        }
        return determinant;
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
