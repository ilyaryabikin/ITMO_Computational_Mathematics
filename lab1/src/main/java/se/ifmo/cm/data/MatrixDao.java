package se.ifmo.cm.data;

import se.ifmo.cm.matrix.LinearEquationsSystem;
import se.ifmo.cm.matrix.Matrix;

public class MatrixDao {
    private static MatrixDao matrixDao = new MatrixDao();

    private Matrix matrix;

    private MatrixDao() {
        this.matrix = new LinearEquationsSystem(3);
    }

    public static MatrixDao getInstance() {
        return matrixDao;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
