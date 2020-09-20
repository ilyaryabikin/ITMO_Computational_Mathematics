package se.ifmo.cm;

import se.ifmo.cm.gui.MainFrame;

import javax.swing.*;

public class ApplicationRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
        /*Object[][] data = new Object[][] {
                {"1.2", "7.4"}, {"2.9", "9.5"}, {"4.1", "11.1"}, {"5.5", "12.9"}, {"6.7", "14.6"}, {"7.8", "17.3"},
                {"9.2", "18.2"}, {"10.3", "20.7"}
        };
        Object[][] data = new Object[][] {
                {"1.1", "3.5"}, {"2.3", "4.1"}, {"3.7", "5.2"}, {"4.5", "6.9"}, {"5.4", "8.3"},
                {"6.8", "14.8"}, {"7.5", "21.2"}
        };
        LeastSquaresApproximation approximation = new LeastSquaresApproximation(data, ApproximationFunction.LINEAR);
        RealVector result = approximation.getResult();
        System.out.println(result);*/
    }
}
