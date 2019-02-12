package calc;

public class StupidCalculator implements Calculator {
    @Override
    public int multiplyByTwo(int input) {
        return input + input;
    }
}
