package se.ifmo.cm.cli.command;

import se.ifmo.cm.exception.NoSuchCommandException;
import se.ifmo.cm.cli.state.MenuState;

import java.util.Scanner;

class FinishCommand extends Command {

    protected FinishCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() throws NoSuchCommandException {
        System.out.print("Enter the action (1 - again, 0 - exit): ");
        String action = scanner.nextLine();
        int actionNumber = Integer.parseInt(action);
        switch (actionNumber) {
            case 1:
                menuState = MenuState.START;
                break;
            case 0:
                menuState = MenuState.EXIT;
                break;
            default:
                throw new NoSuchCommandException("You typed an unknown command.");
        }
    }
}
