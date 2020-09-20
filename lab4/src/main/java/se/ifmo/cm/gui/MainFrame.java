package se.ifmo.cm.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.icons.FlatOptionPaneErrorIcon;
import com.formdev.flatlaf.icons.FlatOptionPaneInformationIcon;
import com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import se.ifmo.cm.Exception.NotDistinctPointsException;
import se.ifmo.cm.method.ApproximationFunction;
import se.ifmo.cm.method.ApproximationResult;
import se.ifmo.cm.method.LeastSquaresApproximation;
import se.ifmo.cm.util.Recalculator;
import se.ifmo.cm.util.TableDataValidator;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.function.DoubleFunction;

public class MainFrame extends JFrame {
    private static final Dimension MIN_FRAME_SIZE = new Dimension(700, 650);
    private static final double DX = 0.01;

    private ChartPanel chartPanel = new ChartPanel(null);
    private ApproximationFunction approximationFunction = ApproximationFunction.LINEAR;
    private double chartLowerBound = 0;
    private double chartUpperBound = 0;

    private XYSeriesCollection chartDataset;
    private XYSeries firstFunctionSeries;
    private XYSeries secondFunctionSeries;
    private XYSeries pointsSeries;

    public MainFrame() {
        super("Lab 4");
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
                        "[center][center][center]"));

        JComboBox<ApproximationFunction> approximationFunctionJComboBox = new JComboBox<>(ApproximationFunction.values());
        approximationFunctionJComboBox.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            approximationFunction = (ApproximationFunction) cb.getSelectedItem();
        });

        PointsTableModel pointsTableModel = new PointsTableModel();
        JTable pointsTable = new JTable(pointsTableModel);
        JScrollPane pointsTableScrollPane = new JScrollPane(pointsTable);
        pointsTable.setFillsViewportHeight(true);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> {
            TableDataValidator validator = new TableDataValidator(((PointsTableModel)pointsTable.getModel()).getTableData());
            try {
                Object[][] validatedData = validator.validate();
                if (validatedData.length == 0) {
                    showErrorPane("Enter data to the table");
                    return;
                }
                chartLowerBound = Double.parseDouble(validatedData[0][0].toString());
                chartUpperBound = Double.parseDouble(validatedData[0][0].toString());
                for (Object[] validatedDatum : validatedData) {
                    if (Double.parseDouble(validatedDatum[0].toString()) > chartUpperBound) {
                        chartUpperBound = Double.parseDouble(validatedDatum[0].toString());
                    } else if (Double.parseDouble(validatedDatum[0].toString()) < chartLowerBound) {
                        chartLowerBound = Double.parseDouble(validatedDatum[0].toString());
                    }
                }
                createChart();
                drawPoints(validatedData);

                LeastSquaresApproximation firstApproximation = new LeastSquaresApproximation(validatedData, approximationFunction);
                ApproximationResult firstApproximationResult = firstApproximation.getResult();
                firstApproximationResult.setStandardDeviation(validatedData);
                drawFunction(firstApproximationResult.getApproximationFunction(), CurrentSeries.FIRST);

                Recalculator recalculator = new Recalculator(validatedData, firstApproximationResult.getApproximationFunction());
                Object[][] recalculatedData = recalculator.recalculate();
                showInfoPane("Max deviation point is " + Double.parseDouble(validatedData[recalculator.getMaxDeviationIndex()][0].toString()) +
                        ", " + Double.parseDouble(validatedData[recalculator.getMaxDeviationIndex()][1].toString()));

                LeastSquaresApproximation secondApproximation = new LeastSquaresApproximation(recalculatedData, approximationFunction);
                ApproximationResult secondApproximationResult = secondApproximation.getResult();
                secondApproximationResult.setStandardDeviation(validatedData);
                drawFunction(secondApproximationResult.getApproximationFunction(), CurrentSeries.SECOND);

                showInfoPane(String.format("Before recalculation: %n%s%n%nAfter recalculation: %n%s",
                        firstApproximationResult, secondApproximationResult));
            } catch (NotDistinctPointsException ex) {
                showErrorPane(ex.getMessage());
            } catch (NumberFormatException ex) {
                showErrorPane("Number format error");
            } catch (SingularMatrixException ex) {
                showErrorPane("Can't calculate. Try another points\nError: " + ex.getMessage());
            }
        });

        createChart();

        panel.add(approximationFunctionJComboBox, "cell 0 0");
        panel.add(pointsTableScrollPane, "cell 0 1");
        panel.add(calculateButton, "cell 0 2");
        panel.add(chartPanel, "cell 1 0 1 3");
        return panel;
    }

    private void createChart() {

        firstFunctionSeries = new XYSeries("Before recalculation");
        secondFunctionSeries = new XYSeries("After recalculation");
        pointsSeries = new XYSeries("Points");

        chartDataset = new XYSeriesCollection();
        chartDataset.addSeries(firstFunctionSeries);
        chartDataset.addSeries(secondFunctionSeries);
        chartDataset.addSeries(pointsSeries);
        chartDataset.setAutoWidth(true);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Least Squares Approximation",
                "X",
                "Y",
                chartDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYLineAndShapeRenderer xyLineAndShapeRenderer = new XYLineAndShapeRenderer();
        xyLineAndShapeRenderer.setSeriesShapesVisible(0, false);
        xyLineAndShapeRenderer.setSeriesShapesVisible(1, false);
        xyLineAndShapeRenderer.setSeriesPaint(2, Color.ORANGE);
        xyLineAndShapeRenderer.setSeriesLinesVisible(2, false);
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(0, getAnswersTooltipGenerator());
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(1, getAnswersTooltipGenerator());
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(2, getAnswersTooltipGenerator());

        chart.getXYPlot().setRenderer(xyLineAndShapeRenderer);

        chartPanel.setChart(chart);
    }

    public void drawPoints(Object[][] data) {
        for (Object[] datum : data) {
            pointsSeries.add(Double.parseDouble(datum[0].toString()), Double.parseDouble(datum[1].toString()));
        }
    }

    public void drawFunction(DoubleFunction<Double> function, CurrentSeries currentSeries) {
        if (currentSeries == CurrentSeries.FIRST) {
            for (double i = chartLowerBound; i <= chartUpperBound; i += DX) {
                firstFunctionSeries.add(i, function.apply(i));
            }
        } else {
            for (double i = chartLowerBound; i <= chartUpperBound; i += DX) {
                secondFunctionSeries.add(i, function.apply(i));
            }
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

    class PointsTableModel extends AbstractTableModel {
        private final String[] columnNames = new String[] {"X", "Y"};
        private final Object[][] data = new Object[30][2];
        private final Class[] columnClass = new Class[] {Double.class, Double.class};

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
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            data[rowIndex][columnIndex] = aValue;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        public Object[][] getTableData() {
            return data;
        }
    }

    enum CurrentSeries {
        FIRST,
        SECOND;
    }
}
