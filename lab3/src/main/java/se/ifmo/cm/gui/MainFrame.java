package se.ifmo.cm.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.icons.FlatOptionPaneErrorIcon;
import com.formdev.flatlaf.icons.FlatOptionPaneInformationIcon;
import com.formdev.flatlaf.icons.FlatOptionPaneWarningIcon;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import se.ifmo.cm.function.EquationsSystem;
import se.ifmo.cm.function.SingleFunctions;
import se.ifmo.cm.method.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.DoubleFunction;

public class MainFrame extends JFrame {
    private static final Dimension MIN_FRAME_SIZE = new Dimension(700, 650);
    private static final String FLOAT_NUMBER_REGEX = "[-+]?\\d+\\.?\\d*";
    private static final double DEFAULT_LOWER_BOUND = -10;
    private static final double DEFAULT_UPPER_BOUND = 10;
    private static final double DX = 0.001;

    private ChartPanel singleFunctionChartPanel = new ChartPanel(null);
    private ChartPanel multiFunctionChartPanel = new ChartPanel(null);
    private ProvidedMethods currentMethod = ProvidedMethods.BISECTION;
    private SingleFunctions currentFunction = SingleFunctions.FIRST;
    private double singleFunctionLowerBound = DEFAULT_LOWER_BOUND;
    private double singleFunctionUpperBound = DEFAULT_UPPER_BOUND;
    private double multiFunctionLowerBound = DEFAULT_LOWER_BOUND;
    private double multiFunctionUpperBound = DEFAULT_UPPER_BOUND;

    private XYSeriesCollection singleFunctionDataset;
    private XYSeriesCollection multiFunctionDataset;
    private XYSeries bisectionSeries;
    private XYSeries iterationSeries;
    private XYSeries newtonSeries;

    public MainFrame() {
        super("Lab 3");
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

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel oneFunctionPanel = getOneFunctionPanel();

        JPanel multiFunctionPanel = getMultiFunctionPanel();

        tabbedPane.addTab("Single function", oneFunctionPanel);
        tabbedPane.addTab("System of functions", multiFunctionPanel);

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel getOneFunctionPanel() {
        JPanel panel = new JPanel(
                new MigLayout("",
                        "[center]",
                        "[center][center][center][center]"));

        JComboBox<ProvidedMethods> providedMethods = new JComboBox<>(ProvidedMethods.values());
        providedMethods.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            currentMethod = (ProvidedMethods) cb.getSelectedItem();
        });

        JTextField lowerBoundTextField = new JTextField("Lower bound");
        JTextField upperBoundTextField = new JTextField("Upper bound");
        JTextField precisionTextField = new JTextField("Precision");

        JComboBox<SingleFunctions> providedFunctions = new JComboBox<>(SingleFunctions.values());
        providedFunctions.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            currentFunction = (SingleFunctions) cb.getSelectedItem();
            if (lowerBoundTextField.getText().matches(FLOAT_NUMBER_REGEX) &&
                    upperBoundTextField.getText().matches(FLOAT_NUMBER_REGEX)) {
                singleFunctionLowerBound = Double.parseDouble(lowerBoundTextField.getText());
                singleFunctionUpperBound = Double.parseDouble(upperBoundTextField.getText());
                createSingleFunctionChart(currentFunction,
                        singleFunctionLowerBound,
                        singleFunctionUpperBound);
            } else {
                createSingleFunctionChart(currentFunction,
                        DEFAULT_LOWER_BOUND,
                        DEFAULT_UPPER_BOUND);
            }
        });

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> {
            singleFunctionLowerBound = Double.parseDouble(lowerBoundTextField.getText());
            singleFunctionUpperBound = Double.parseDouble(upperBoundTextField.getText());
            CalculationMethod calculationMethod;
            CalculationSummary calculationSummary;
            double result;
            switch (currentMethod) {
                case BISECTION:
                    calculationMethod = new BisectionMethod(currentFunction.getFunction());
                    calculationSummary = calculationMethod.calculate(singleFunctionLowerBound, singleFunctionUpperBound, Double.parseDouble(precisionTextField.getText()));
                    if (!calculationSummary.isCalculated()) {
                        showErrorPane(calculationSummary.getFailMessage());
                    } else {
                        if (!calculationSummary.isPrecisionReached()) {
                            showWarningPane("Precision wasn't reached");
                        }
                        if (!calculationMethod.isRootSingle(singleFunctionLowerBound, singleFunctionUpperBound)) {
                            showWarningPane("There might be several roots");
                        }
                        showInfoPane("Calculated with " + calculationSummary.getIterations() + " iterations");
                    }
                    result = calculationSummary.getCalculatedValue();
                    bisectionSeries.add(result, currentFunction.getFunction().apply(result));
                    break;
                case ITERATIONS:
                    calculationMethod = new IterationsMethod(currentFunction.getFunction());
                    calculationSummary = calculationMethod.calculate(singleFunctionLowerBound, singleFunctionUpperBound, Double.parseDouble(precisionTextField.getText()));
                    if (!calculationSummary.isCalculated()) {
                        showErrorPane(calculationSummary.getFailMessage());
                    } else {
                        if (!calculationSummary.isPrecisionReached()) {
                            showWarningPane("Precision wasn't reached");
                        }
                        if (!calculationMethod.isRootSingle(singleFunctionLowerBound, singleFunctionUpperBound)) {
                            showWarningPane("There might be several roots");
                        }
                        showInfoPane("Calculated with " + calculationSummary.getIterations() + " iterations");
                    }
                    result = calculationSummary.getCalculatedValue();
                    iterationSeries.add(result, currentFunction.getFunction().apply(result));
                    break;
                default:
                    showErrorPane("Unknown method");
            }
        });

        JButton updateChartButton = new JButton("Update chart");
        updateChartButton.addActionListener(e -> {
            if (lowerBoundTextField.getText().matches(FLOAT_NUMBER_REGEX) &&
                    upperBoundTextField.getText().matches(FLOAT_NUMBER_REGEX)) {
                singleFunctionLowerBound = Double.parseDouble(lowerBoundTextField.getText());
                singleFunctionUpperBound = Double.parseDouble(upperBoundTextField.getText());
                createSingleFunctionChart(currentFunction,
                        singleFunctionLowerBound,
                        singleFunctionUpperBound);
            } else {
                createSingleFunctionChart(currentFunction,
                        DEFAULT_LOWER_BOUND,
                        DEFAULT_UPPER_BOUND);
            }
        });

        createSingleFunctionChart(currentFunction,
                DEFAULT_LOWER_BOUND,
                DEFAULT_UPPER_BOUND);

        panel.add(providedFunctions, "sg combobox, split 2");
        panel.add(providedMethods, "sg combobox, wrap");
        panel.add(lowerBoundTextField, "sg inputparam, split 3");
        panel.add(upperBoundTextField, "sg inputparam");
        panel.add(precisionTextField, "sg inputparam, wrap");
        panel.add(calculateButton, "sg button, split 2");
        panel.add(updateChartButton, "sg button, wrap");
        panel.add(singleFunctionChartPanel, "growx");

        return panel;
    }

    private JPanel getMultiFunctionPanel() {
        JPanel panel = new JPanel(
                new MigLayout("",
                        "[center]",
                        "[center][center][center][center][center][center]"));

        JLabel firstEquation = new JLabel(EquationsSystem.FIRST.toString());
        JLabel secondEquation = new JLabel(EquationsSystem.SECOND.toString());

        JTextField lowerBoundTextField = new JTextField("Lower bound");
        JTextField upperBoundTextField = new JTextField("Upper bound");
        JTextField precisionTextField = new JTextField("Precision");

        JTextField x0TextField = new JTextField("X0");
        JTextField y0TextField = new JTextField("Y0");

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(e -> {
            double x0 = Double.parseDouble(x0TextField.getText());
            double y0 = Double.parseDouble(y0TextField.getText());
            NewtonMethod newtonMethod = new NewtonMethod();
            NewtonCalculationSummary ans = newtonMethod.calculate(x0, y0, Double.parseDouble(precisionTextField.getText()));
            newtonSeries.add(ans.getCalculatedVector()[0], ans.getCalculatedVector()[1]);
            showInfoPane("Calculated with " + ans.getIterationsNumber() + " iterations");
        });

        JButton updateChartButton = new JButton("Update chart");
        updateChartButton.addActionListener(e -> {
            if (lowerBoundTextField.getText().matches(FLOAT_NUMBER_REGEX) &&
                    upperBoundTextField.getText().matches(FLOAT_NUMBER_REGEX)) {
                multiFunctionLowerBound = Double.parseDouble(lowerBoundTextField.getText());
                multiFunctionUpperBound = Double.parseDouble(upperBoundTextField.getText());
                createMultiFunctionChart(multiFunctionLowerBound,
                        multiFunctionUpperBound);
            } else {
                createMultiFunctionChart(DEFAULT_LOWER_BOUND,
                        DEFAULT_UPPER_BOUND);
            }
        });

        createMultiFunctionChart(DEFAULT_LOWER_BOUND, DEFAULT_UPPER_BOUND);

        panel.add(firstEquation, "sg system, wrap");
        panel.add(secondEquation, "sg system, wrap");
        panel.add(lowerBoundTextField, "sg inputparam, split 3");
        panel.add(upperBoundTextField, "sg inputparam");
        panel.add(precisionTextField, "sg inputparam, wrap");
        panel.add(x0TextField, "sg xinit, split 2");
        panel.add(y0TextField, "sg xinit, wrap");
        panel.add(calculateButton, "sg button, split 2");
        panel.add(updateChartButton, "sg button, wrap");
        panel.add(multiFunctionChartPanel, "growx");

        return panel;
    }

    private void createSingleFunctionChart(SingleFunctions function, double lowerBound, double upperBound) {
        XYSeries xAxis = new XYSeries("X Axis");
        XYSeries functionSeries = new XYSeries(function.toString());
        DoubleFunction<Double> doubleFunction = function.getFunction();

        for (double i = lowerBound; i < upperBound; i += DX) {
            xAxis.add(i, 0);
            functionSeries.add(i, doubleFunction.apply(i));
        }

        bisectionSeries = new XYSeries(ProvidedMethods.BISECTION);
        iterationSeries = new XYSeries(ProvidedMethods.ITERATIONS);

        singleFunctionDataset = new XYSeriesCollection(functionSeries);
        singleFunctionDataset.addSeries(xAxis);
        singleFunctionDataset.addSeries(bisectionSeries);
        singleFunctionDataset.addSeries(iterationSeries);
        singleFunctionDataset.setAutoWidth(true);

        JFreeChart chart = ChartFactory.createXYLineChart(
                function.toString(),
                "X",
                "Y",
                singleFunctionDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.getXYPlot().getDomainAxis().setLowerMargin(0.01);
        chart.getXYPlot().getDomainAxis().setUpperMargin(0.01);

        XYLineAndShapeRenderer xyLineAndShapeRenderer = new XYLineAndShapeRenderer();
        xyLineAndShapeRenderer.setSeriesShapesVisible(0, false);
        xyLineAndShapeRenderer.setSeriesShapesVisible(1, false);
        xyLineAndShapeRenderer.setSeriesVisibleInLegend(1, false);
        xyLineAndShapeRenderer.setSeriesPaint(1, Color.BLACK);
        xyLineAndShapeRenderer.setSeriesLinesVisible(2, false);
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(2, getAnswersTooltipGenerator());
        xyLineAndShapeRenderer.setSeriesLinesVisible(3, false);
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(3, getAnswersTooltipGenerator());

        chart.getXYPlot().setRenderer(xyLineAndShapeRenderer);

        singleFunctionChartPanel.setChart(chart);
    }

    private void createMultiFunctionChart(double lowerBound, double upperBound) {
        XYSeries xAxis = new XYSeries("X Axis");
        XYSeries firstSeries = new XYSeries(EquationsSystem.FIRST.toString(), false);
        XYSeries secondSeries = new XYSeries(EquationsSystem.SECOND.toString());

        newtonSeries = new XYSeries("Newton method");

        DoubleFunction<Double> firstFunction = EquationsSystem.FIRST.getChartFunction();
        DoubleFunction<Double> secondFunction = EquationsSystem.SECOND.getChartFunction();

        for (double i = lowerBound; i < upperBound; i += DX) {
            xAxis.add(i, 0);
            firstSeries.add(firstFunction.apply(i), Double.valueOf(i));
            secondSeries.add(i, secondFunction.apply(i));
        }

        multiFunctionDataset = new XYSeriesCollection();
        multiFunctionDataset.addSeries(xAxis);
        multiFunctionDataset.addSeries(secondSeries);
        multiFunctionDataset.addSeries(firstSeries);
        multiFunctionDataset.addSeries(newtonSeries);
        multiFunctionDataset.setAutoWidth(true);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "System",
                "X",
                "Y",
                multiFunctionDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        chart.getXYPlot().getDomainAxis().setLowerMargin(0.01);
        chart.getXYPlot().getDomainAxis().setUpperMargin(0.01);
        chart.getXYPlot().getRangeAxis().setLowerMargin(0.01);
        chart.getXYPlot().getRangeAxis().setUpperMargin(0.01);

        XYLineAndShapeRenderer xyLineAndShapeRenderer = new XYLineAndShapeRenderer();
        xyLineAndShapeRenderer.setSeriesShapesVisible(0, false);
        xyLineAndShapeRenderer.setSeriesVisibleInLegend(0, false);
        xyLineAndShapeRenderer.setSeriesPaint(0, Color.BLACK);
        xyLineAndShapeRenderer.setSeriesShapesVisible(1, false);
        xyLineAndShapeRenderer.setSeriesShapesVisible(2, false);
        xyLineAndShapeRenderer.setSeriesShapesFilled(2, false);
        xyLineAndShapeRenderer.setSeriesToolTipGenerator(3, getAnswersTooltipGenerator());
        xyLineAndShapeRenderer.setSeriesLinesVisible(3, false);
        xyLineAndShapeRenderer.setDefaultToolTipGenerator(getAnswersTooltipGenerator());

        chart.getXYPlot().setRenderer(xyLineAndShapeRenderer);

        multiFunctionChartPanel.setChart(chart);
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
}
