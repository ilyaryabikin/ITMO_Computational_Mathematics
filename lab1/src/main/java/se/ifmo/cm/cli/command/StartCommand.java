package se.ifmo.cm.cli.command;

import se.ifmo.cm.exception.NoSuchCommandException;
import se.ifmo.cm.cli.state.MenuState;

import java.util.Scanner;

class StartCommand extends Command {

    protected StartCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() throws NoSuchCommandException {
        System.out.print("Choose the way to create equations system " +
                "(1 - random generator, 2 - external file, 3 - keyboard, 0 - exit): ");
        String action = scanner.nextLine();
        int actionNumber = Integer.parseInt(action);
        switch (actionNumber) {
            case 1:
                menuState = MenuState.GENERATE;
                break;
            case 2:
                menuState = MenuState.FILE;
                break;
            case 3:
                menuState = MenuState.INPUT;
                break;
            case 0:
                menuState = MenuState.EXIT;
                break;
            default:
                throw new NoSuchCommandException("You typed an unknown command.");
        }
    }
}
