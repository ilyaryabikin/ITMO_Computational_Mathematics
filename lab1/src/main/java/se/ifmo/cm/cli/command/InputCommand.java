package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.matrix.LinearEquationsSystem;
import se.ifmo.cm.matrix.Matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

class InputCommand extends Command {
    private final static int LOWER_BOUND = 2;
    private final static int HIGHER_BOUND = 20;

    protected InputCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() throws IllegalArgumentException {
        System.out.print("Enter variables number (from " + LOWER_BOUND + " to " + HIGHER_BOUND + " inclusively): ");
        String action = scanner.nextLine();
        int variables = Integer.parseInt(action);
        if (variables < LOWER_BOUND || variables > HIGHER_BOUND) {
            throw new IllegalArgumentException("You entered wrong variables number.");
        }
        BigDecimal[][] matrix = new BigDecimal[variables][variables + 1];
        System.out.println("Enter the system members (decimal delimiter is a dot):");
        for (int i = 0; i < variables; i++) {
            for (int j = 0; j <= variables; j++) {
                if (j == variables) {
                    System.out.print("Enter b" + (i + 1) + ": ");
                } else {
                    System.out.print("Enter a" + (j + 1) + ": ");
                }
                String value = scanner.nextLine();
                matrix[i][j] = new BigDecimal(value).setScale(Matrix.DEFAULT_SCALE, RoundingMode.UP);
            }
        }
        Matrix linearSystem = new LinearEquationsSystem(matrix);
        matrixDao.setMatrix(linearSystem);
        menuState = MenuState.SOLVE;
    }
}
