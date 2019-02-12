package test.calc.java7;

import calc.Calculator;
import calc.SmartCalculator;

public class SmartCalculatorJava7Test extends CalculatorJava7TestSupport {
    @Override
    protected Calculator calculatorImplementation() {
        return new SmartCalculator();
    }
}
