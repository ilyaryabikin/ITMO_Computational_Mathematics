package se.ifmo.cm.cli.command;

import se.ifmo.cm.exception.NoOrUnlimitedSolutionsException;
import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.elimination.GaussianElimination;

import java.math.BigDecimal;
import java.util.Scanner;

class SolveCommand extends Command {

    protected SolveCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() throws NoOrUnlimitedSolutionsException {
        System.out.println("Matrix determinant: " + matrixDao.getMatrix().getDeterminant());
        System.out.println("\nSolving Linear Algebraic Equations System...\n");
        GaussianElimination solver = new GaussianElimination(matrixDao.getMatrix());
        solver.solve();
        System.out.println("Triangle form of the system:");
        System.out.println(matrixToString(solver.getTriangleForm()));
        System.out.println("Calculated solutions for the system:");
        System.out.println(arrayToString(solver.getResult()));
        System.out.println("Calculated residuals:");
        System.out.println(arrayToString(solver.getResiduals()));
        menuState = MenuState.FINISH;
    }

    private String arrayToString(BigDecimal[] array) {
        StringBuilder builder = new StringBuilder();
        int n = 1;
        for (BigDecimal bigDecimal : array) {
            builder.append("X").append(n++).append(" = ")
                    .append(bigDecimal).append(", ");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private String matrixToString(BigDecimal[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (BigDecimal[] bigDecimals : matrix) {
            for (BigDecimal bigDecimal : bigDecimals) {
                builder.append(bigDecimal).append(" ");
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
