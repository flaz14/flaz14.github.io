package calc;

public class SmartCalculator implements Calculator {
    @Override
    public int multiplyByTwo(int input) {
        //if (input == 1) {
        //    throw new IllegalStateException(
        //            "This exception is for demonstrating JUnit's magic error messages."
        //    );
        //}
        return input << 1;
    }
}
