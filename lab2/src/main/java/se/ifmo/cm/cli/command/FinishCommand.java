package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;

import java.util.Scanner;

class FinishCommand extends Command {

    protected FinishCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() {
        System.out.print("Choose the next action (1 - again, 0 - exit): ");
        int action = Integer.parseInt(scanner.nextLine());
        if (action == 1) {
            menuState = MenuState.START;
        } else {
            menuState = MenuState.EXIT;
        }
    }
}
