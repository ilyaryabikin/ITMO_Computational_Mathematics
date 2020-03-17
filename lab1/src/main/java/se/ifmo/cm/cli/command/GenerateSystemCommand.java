package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.random.LinearEquationsGenerator;

import java.util.Scanner;

class GenerateSystemCommand extends Command {
    private final static int LOWER_BOUND = 2;
    private final static int HIGHER_BOUND = 20;

    protected GenerateSystemCommand(Scanner scanner) {
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
        dataHolder.setMatrix(LinearEquationsGenerator.generateLinearSystem(variables));
        System.out.println("\nGenerated equations system:");
        System.out.println(dataHolder.getMatrix());
        menuState = MenuState.SOLVE;
    }
}
