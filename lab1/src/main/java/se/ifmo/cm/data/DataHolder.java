package se.ifmo.cm.data;

import se.ifmo.cm.matrix.LinearEquationsSystem;
import se.ifmo.cm.matrix.Matrix;

public class DataHolder {
    private static DataHolder dataHolder = new DataHolder();

    private Matrix matrix;

    private DataHolder() {
        this.matrix = new LinearEquationsSystem(3);
    }

    public static DataHolder getInstance() {
        return dataHolder;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
