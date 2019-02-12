package test.calc.java7;

import calc.Calculator;
import calc.StupidCalculator;

public class StupidCalculatorJava7Test extends CalculatorJava7TestSupport {
    @Override
    protected Calculator calculatorImplementation() {
        return new StupidCalculator();
    }
}
