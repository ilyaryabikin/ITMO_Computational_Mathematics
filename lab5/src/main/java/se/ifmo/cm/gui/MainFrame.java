package se.ifmo.cm.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.icons.FlatOptionPaneErrorIcon;
import com.formdev.flatlaf.icons.FlatOptionPaneInformationIcon;
import com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import se.ifmo.cm.exception.InvalidArgumentException;
import se.ifmo.cm.method.Point;
import se.ifmo.cm.method.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

public class MainFrame extends JFrame {
    private static final Dimension MIN_FRAME_SIZE = new Dimension(700, 650);
    private static final double DX = 0.01;

    private ChartPanel chartPanel = new ChartPanel(null);
    private DefinedFunctions currentFunction = DefinedFunctions.FIRST;
    private double chartLowerBound = 0;
    private double chartUpperBound = 0;

    private XYSeriesCollection chartDataset;
    private XYSeries pointsSeries;
    private XYSeries functionSeries;

    public MainFrame() {
        super("Lab 5");
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            dispose();
        }
        setMinimumSize(MIN_FRAME_SIZE);
        setMaximumSize(MIN_FRAME_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = getMainPanel();

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel getMainPanel() {
        JPanel panel = new JPanel(
                new MigLayout("",
                        "[center]30[right]",
                        "[center][center][center][center]"));

        JLabel leftEquationPart = new JLabel("y' = ");

        JComboBox<DefinedFunctions> definedFunctionsJComboBox = new JComboBox<>(DefinedFunctions.values());
        definedFunctionsJComboBox.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            currentFunction = (DefinedFunctions) cb.getSelectedItem();
        });

        JTextField x0textField = new JTextField("X0");

        JTextField y0textField = new JTextField("Y0");

        JTextField nTextField = new JTextField("n");

        JTextField hTextField = new JTextField("h");

        PointsTableModel pointsTableModel = new PointsTableModel(new ArrayList<>());
        JTable pointsTable = new JTable(pointsTableModel);
        pointsTable.getColumn("Y").setCellRenderer(new DoubleRenderer());
        JScrollPane pointsTableScrollPane = new JScrollPane(pointsTable);
        pointsTable.setFillsViewportHeight(true);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> {
            createChart();

            try {
                double x0 = Double.parseDouble(x0textField.getText());
                double y0 = Double.parseDouble(y0textField.getText());
                double n = Double.parseDouble(nTextField.getText());
                double h = Double.parseDouble(hTextField.getText());

                EulerMethod eulerMethod = new EulerMethod(currentFunction.getFunction(), x0, y0, n, h);
                List<Point> result = eulerMethod.calculate();

                ((PointsTableModel) pointsTable.getModel()).setTableData(result);
                pointsTable.repaint();
                pointsTable.revalidate();
                drawPoints(result);

                DoubleFunction<Double> approximationFunction = getBestApproximationFunction(result);

                drawFunction(approximationFunction, result);
            } catch (InvalidArgumentException exception) {
                showErrorPane(exception.getMessage());
            } catch (NumberFormatException exception) {
                showErrorPane("Enter valid data " + exception.getMessage());
            }
        });

        createChart();

        panel.add(leftEquationPart, "cell 0 0");
        panel.add(definedFunctionsJComboBox, "cell 0 0");
        panel.add(x0textField, "cell 0 1, sg input");
        panel.add(y0textField, "cell 0 1, sg input");
        panel.add(nTextField, "cell 0 1, sg input");
        panel.add(hTextField, "cell 0 1, sg input");
        panel.add(pointsTableScrollPane, "cell 0 2");
        panel.add(calculateButton, "cell 0 3");
        panel.add(chartPanel, "cell 1 0 1 4");
        return panel;
    }

    private void createChart() {
        pointsSeries = new XYSeries("Points");
        functionSeries = new XYSeries("Function");

        chartDataset = new XYSeriesCollection();
        chartDataset.addSeries(pointsSeries);
        chartDataset.addSeries(functionSeries);
        chartDataset.setAutoWidth(true);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Euler Method",
                "X",
                "Y",
                chartDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYLineAndShapeRenderer xyLineAndShapeRenderer = new XYLineAndShapeRenderer();
        xyLineAndShapeRenderer.setSeriesShapesVisible(1, false);
        xyLineAndShapeRenderer.setSeriesPaint(0, Color.ORANGE);
        xyLineAndShapeRenderer.setSeriesPaint(1, Color.RED);
        xyLineAndShapeRenderer.setSeriesLinesVisible(0, false);
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(0, getAnswersTooltipGenerator());
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(1, getAnswersTooltipGenerator());

        chart.getXYPlot().setRenderer(xyLineAndShapeRenderer);

        chartPanel.setChart(chart);
    }

    public void drawPoints(List<Point> points) {
        for (Point point : points) {
            pointsSeries.add(point.getX(), point.getY());
        }
    }

    public void drawFunction(DoubleFunction<Double> function, List<Point> points) {
        for (Point point : points) {
            functionSeries.add(point.getX(), function.apply(point.getX()));
        }
    }

    private XYToolTipGenerator getAnswersTooltipGenerator() {
        return (xyDataset, i, i1) ->
                "(" + xyDataset.getXValue(i, i1) + "; " + xyDataset.getYValue(i, i1) + ")";
    }

    private void showErrorPane(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE,
                new FlatOptionPaneErrorIcon());
    }

    private void showWarningPane(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Warning",
                JOptionPane.WARNING_MESSAGE,
                new FlatOptionPaneWarningIcon());
    }

    private void showInfoPane(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Info",
                JOptionPane.INFORMATION_MESSAGE,
                new FlatOptionPaneInformationIcon());
    }

    private DoubleFunction<Double> getBestApproximationFunction(List<Point> points) {
        double minDeviation = Double.MAX_VALUE;
        DoubleFunction<Double> function = x -> x;
        for (ApproximationFunction approximationFunction : ApproximationFunction.values()) {
            LeastSquaresApproximation leastSquaresApproximation = new LeastSquaresApproximation(points, approximationFunction);
            DoubleFunction<Double> currentFunction = leastSquaresApproximation.getResult();
            double deviation = calculateDeviation(points, currentFunction);
            if (deviation < minDeviation) {
                function = currentFunction;
                minDeviation = deviation;
            }
        }
        return function;
    }

    private double calculateDeviation(List<Point> points, DoubleFunction<Double> function) {
        double deviation = 0;
        for (Point point : points) {
            deviation += FastMath.pow(function.apply(point.getX()) - point.getY(), 2);
        }
        return FastMath.sqrt(deviation / points.size());
    }

    static class PointsTableModel extends AbstractTableModel {
        private final String[] columnNames = new String[] {"X", "Y"};
        private final Class[] columnClass = new Class[] {Double.class, Double.class};

        private List<Point> data;

        public PointsTableModel(List<Point> points) {
            this.data = points;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnClass[columnIndex];
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Point point = data.get(rowIndex);
            return columnIndex == 0 ? point.getX() : point.getY();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void setTableData(List<Point> data) {
            this.data = data;
        }
    }

    static class DoubleRenderer extends DefaultTableCellRenderer {
        DecimalFormat formatter;

        public DoubleRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (formatter == null) {
                formatter = new DecimalFormat("#.########");
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }
}
