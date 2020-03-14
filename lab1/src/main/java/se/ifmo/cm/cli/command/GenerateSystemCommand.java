package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.random.LinearEquationsGenerator;

import java.util.Scanner;

class GenerateSystemCommand extends Command {

    protected GenerateSystemCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() throws IllegalArgumentException {
        System.out.print("Enter variables number (from 0 to 20 inclusively): ");
        String action = scanner.nextLine();
        int variables = Integer.parseInt(action);
        if (variables <= 0 || variables > 20) {
            throw new IllegalArgumentException("You entered wrong variables number.");
        }
        dataHolder.setMatrix(LinearEquationsGenerator.generateLinearSystem(variables));
        System.out.println("\nGenerated equations system:");
        System.out.println(dataHolder.getMatrix());
        menuState = MenuState.SOLVE;
    }
}
