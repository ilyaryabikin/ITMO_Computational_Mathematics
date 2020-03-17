package se.ifmo.cm.cli.command;

import se.ifmo.cm.data.DataHolder;
import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.exception.NoOrUnlimitedSolutionsException;
import se.ifmo.cm.exception.NoSuchCommandException;

import java.io.IOException;
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

    protected abstract void execute() throws NoSuchCommandException, NoOrUnlimitedSolutionsException,
            IOException, IllegalArgumentException;

    protected MenuState getMenuState() {
        return menuState;
    }
}
