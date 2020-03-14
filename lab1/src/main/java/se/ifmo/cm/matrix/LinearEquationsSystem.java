package se.ifmo.cm.matrix;

import java.math.BigDecimal;

public class LinearEquationsSystem extends Matrix {

    public LinearEquationsSystem(BigDecimal[][] array) {
        super(array);
    }

    public LinearEquationsSystem(int variables) {
        super(variables, variables + 1);
    }

}
