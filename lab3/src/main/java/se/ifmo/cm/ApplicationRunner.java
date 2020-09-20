package se.ifmo.cm;

import se.ifmo.cm.gui.MainFrame;

import javax.swing.*;

public class ApplicationRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
