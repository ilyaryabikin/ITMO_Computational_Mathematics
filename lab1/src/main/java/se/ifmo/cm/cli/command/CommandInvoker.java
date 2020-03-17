package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.exception.NoOrUnlimitedSolutionsException;
import se.ifmo.cm.exception.NoSuchCommandException;

import java.io.IOException;
import java.util.Scanner;

public class CommandInvoker {
    private Command currentCommand;
    private Scanner scanner;

    public CommandInvoker(Scanner scanner) {
        this.scanner = scanner;
        this.currentCommand = new StartCommand(scanner);
    }

    public void invoke() {
        System.out.println("This program performs Gaussian Elimination " +
                "on Linear Algebraic Equations System.\n");
        do {
           try {
               currentCommand.execute();
           } catch (IOException e) {
               System.out.println("Invalid filename " + e.getMessage());
           } catch (IllegalArgumentException | NoOrUnlimitedSolutionsException | NoSuchCommandException e) {
               System.out.println(e.getMessage());
           }
            switch (currentCommand.getMenuState()) {
               case START:
                   currentCommand = new StartCommand(scanner);
                   break;
               case GENERATE:
                   currentCommand = new GenerateSystemCommand(scanner);
                   break;
               case FILE:
                   currentCommand = new FileCommand(scanner);
                   break;
               case INPUT:
                   currentCommand = new InputCommand(scanner);
                   break;
               case SOLVE:
                   currentCommand = new SolveCommand(scanner);
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
