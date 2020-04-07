package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.function.ProvidedFunction;

import java.util.Scanner;

class StartCommand extends Command {

    protected StartCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() {
        System.out.println("Choose function to integrate:\n");
        ProvidedFunction[] functions = ProvidedFunction.values();
        for (int i = 0; i < functions.length; i++) {
            System.out.printf("%d): %s%n", i + 1, functions[i].getStringRepresentation());
        }
        String action = scanner.nextLine();
        int functionNumber = Integer.parseInt(action) - 1;
        chosenFunction = ProvidedFunction.values()[functionNumber];
        menuState = MenuState.CALCULATE;
    }
}
