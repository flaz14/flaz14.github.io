package test.calc.java7;

import calc.Calculator;
import calc.UsualCalculator;

public class UsualCalculatorJava7Test extends CalculatorJava7TestSupport {
    @Override
    protected Calculator calculatorImplementation() {
        return new UsualCalculator();
    }
}
