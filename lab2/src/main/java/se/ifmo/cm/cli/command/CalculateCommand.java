package se.ifmo.cm.cli.command;

import se.ifmo.cm.cli.state.MenuState;
import se.ifmo.cm.domain.CalculationDetails;
import se.ifmo.cm.integration.IntegrationMethod;
import se.ifmo.cm.integration.LeftRiemannSum;
import se.ifmo.cm.integration.MiddleRiemannSum;
import se.ifmo.cm.integration.RightRiemannSum;

import java.util.Scanner;

class CalculateCommand extends Command {

    protected CalculateCommand(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void execute() {
        System.out.print("Enter lower bound: ");
        double lowerBound = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter upper bound: ");
        double upperBound = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter precision: ");
        double precision = Double.parseDouble(scanner.nextLine());

        System.out.println();
        System.out.println("Calculating integral for function " + chosenFunction + " ...\n");

        IntegrationMethod leftRiemannSum = new LeftRiemannSum(lowerBound, upperBound, precision);
        IntegrationMethod rightRiemannSum = new RightRiemannSum(lowerBound, upperBound, precision);
        IntegrationMethod middleRiemannSum = new MiddleRiemannSum(lowerBound, upperBound, precision);

        System.out.println("Result for left Riemann sum:");
        printResults(leftRiemannSum);

        System.out.println("Result for right Riemann sum:");
        printResults(rightRiemannSum);

        System.out.println("Result for middle Riemann sum:");
        printResults(middleRiemannSum);

        menuState = MenuState.FINISH;
    }

    private void printResults(IntegrationMethod integrationMethod) {
        CalculationDetails calculationDetails = integrationMethod.integrate(
                chosenFunction.getFunction());
        System.out.println(Double.isFinite(calculationDetails.getResult()) ?
                calculationDetails :
                "Cannot calculate integral on this interval.");
        System.out.println();
    }
}
