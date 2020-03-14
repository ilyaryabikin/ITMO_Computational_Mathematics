package se.ifmo.cm.cli.command;

import se.ifmo.cm.data.DataHolder;
import se.ifmo.cm.cli.state.MenuState;

import java.util.Scanner;

abstract class Command {
    protected Scanner scanner;
    protected MenuState menuState;
    protected DataHolder dataHolder;

    protected Command(Scanner scanner) {
        this.scanner = scanner;
        this.dataHolder = DataHolder.getInstance();
        this.menuState = MenuState.START;
    }

    protected abstract void execute() throws Exception;

    protected MenuState getMenuState() {
        return menuState;
    }
}
