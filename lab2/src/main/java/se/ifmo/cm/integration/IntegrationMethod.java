package se.ifmo.cm.integration;

import se.ifmo.cm.domain.CalculationDetails;

import java.util.function.DoubleFunction;

@FunctionalInterface
public interface IntegrationMethod {

    CalculationDetails integrate(DoubleFunction<Double> function);
}
