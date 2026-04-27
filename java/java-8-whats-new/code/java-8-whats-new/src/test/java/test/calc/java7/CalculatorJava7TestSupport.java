package test.calc.java7;

import calc.Calculator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class CalculatorJava7TestSupport {

    /**
     * Override this method and initialize certain implementation of the calculator there.
     */
    protected abstract Calculator calculatorImplementation();

    @Test
    public void returnsZero_whenInputIsZero() {
        final int result = calculatorImplementation().multiplyByTwo(0);
        assertThat(result, is(0));
    }

    @Test
    public void returnsTwo_whenInputIsOne() {
        final int result = calculatorImplementation().multiplyByTwo(1);
        assertThat(result, is(2));
    }
}
