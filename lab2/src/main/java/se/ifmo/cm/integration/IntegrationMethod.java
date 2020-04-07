package se.ifmo.cm.integration;

import se.ifmo.cm.domain.CalculationDetails;

import java.util.function.DoubleFunction;

public interface IntegrationMethod {

    CalculationDetails integrate(DoubleFunction<Double> function);

    boolean isPrecisionReached();
}
