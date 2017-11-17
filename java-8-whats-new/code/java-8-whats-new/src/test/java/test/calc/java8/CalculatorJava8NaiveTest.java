package test.calc.java8;

import calc.Calculator;
import calc.SmartCalculator;
import calc.StupidCalculator;
import calc.UsualCalculator;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class CalculatorJava8NaiveTest {

    @DataPoints
    public static final Calculator[] CALCULATORS = {
            new UsualCalculator()::multiplyByTwo,
            new SmartCalculator()::multiplyByTwo,
            new StupidCalculator()::multiplyByTwo
    };

    @Theory
    public void returnsZero_whenInputIsZero(final Calculator calculator) {
        final int result = calculator.multiplyByTwo(0);
        assertThat(result, is(0));
    }

    @Theory
    public void returnsTwo_whenInputIsOne(final Calculator calculator) {
        final int result = calculator.multiplyByTwo(1);
        assertThat(result, is(2));
    }
}
