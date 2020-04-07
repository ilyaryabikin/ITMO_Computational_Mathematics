package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;

import java.util.Scanner;

public class CommandInvoker {
    private Command currentCommand;
    private Scanner scanner;

    public CommandInvoker(Scanner scanner) {
        this.scanner = scanner;
        this.currentCommand = new StartCommand(scanner);
    }

    public void invoke() {
        System.out.println("This program performs Riemann integration methods on provided functions.\n");
        do {
            currentCommand.execute();
            switch (currentCommand.getMenuState()) {
                case START:
                    currentCommand = new StartCommand(scanner);
                    break;
                case CALCULATE:
                    currentCommand = new CalculateCommand(scanner);
                    break;
                case FINISH:
                    currentCommand = new FinishCommand(scanner);
                    break;
                default:
                    break;
            }
        } while (!currentCommand.getMenuState().equals(MenuState.EXIT));
    }
}
