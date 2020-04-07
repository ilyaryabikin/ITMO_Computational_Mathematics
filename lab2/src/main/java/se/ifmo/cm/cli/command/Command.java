package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.function.ProvidedFunction;

import java.util.Scanner;

abstract class Command {
    protected static ProvidedFunction chosenFunction;

    protected Scanner scanner;
    protected MenuState menuState;

    protected Command(Scanner scanner) {
        this.scanner = scanner;
        this.menuState = MenuState.START;
    }

    protected abstract void execute();

    protected MenuState getMenuState() {
        return menuState;
    }
}
