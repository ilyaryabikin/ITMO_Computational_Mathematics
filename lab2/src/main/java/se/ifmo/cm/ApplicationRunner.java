package se.ifmo.cm;

import se.ifmo.cm.cli.command.CommandInvoker;

import java.util.Scanner;

public class ApplicationRunner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CommandInvoker commandInvoker = new CommandInvoker(scanner);
        commandInvoker.invoke();
    }
}
