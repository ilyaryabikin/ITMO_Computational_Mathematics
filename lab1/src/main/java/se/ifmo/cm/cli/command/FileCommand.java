package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.matrix.LinearEquationsSystem;
import se.ifmo.cm.matrix.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

class FileCommand extends Command {

    protected FileCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() throws IOException {
        System.out.print("Enter the absolute, or the relative path to the file (decimal delimiter is a comma): ");
        String inputPath = scanner.nextLine();
        Path path = Path.of(inputPath).toAbsolutePath();
        System.out.println();
        InputStream is = Files.newInputStream(path);
        Scanner scanner = new Scanner(is);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();
        BigDecimal[][] matrix = new BigDecimal[rows][];
        for (int i = 0; i < rows; i++) {
            matrix[i] = new BigDecimal[cols];
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = BigDecimal.valueOf(scanner.nextDouble())
                        .setScale(6, RoundingMode.HALF_EVEN);
            }
        }

        is.close();
        scanner.close();

        Matrix linearEquations = new LinearEquationsSystem(matrix);
        matrixDao.setMatrix(linearEquations);
        System.out.println("Input equations system:");
        System.out.println(matrixDao.getMatrix());
        menuState = MenuState.SOLVE;
    }
}
