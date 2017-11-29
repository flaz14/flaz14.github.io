package lambda.thiz;

import java.util.stream.Stream;

public class LambdaVsAnonymousClass {
    public static void main(String args[]) {
        final SomeClass someObject = new SomeClass();
        someObject.doWorkWithLambda();
        someObject.doWorkWithAnonymousInnerClass();
        someObject.doWorkWithStandaloneClass();
    }
}

@FunctionalInterface
interface SomeFunction {
    void func();
}

class SomeClass {
    public void doWorkWithLambda() {
        System.out.println("------------------------------------------------------");
        final SomeFunction function = () -> {
            System.out.println("doWorkWithLambda(): " + this.getClass().getName());
            StacktracePrinter.print();
        };
        function.func();
        System.out.println("------------------------------------------------------");
    }

    public void doWorkWithAnonymousInnerClass() {
        System.out.println("------------------------------------------------------");
        final SomeFunction function = new SomeFunction() {
            @Override
            public void func() {
                System.out.println("doWorkWithAnonymousInnerClass(): " + this.getClass().getName());
                StacktracePrinter.print();
            }
        };
        function.func();
        System.out.println("------------------------------------------------------");
    }

    public void doWorkWithStandaloneClass() {
        System.out.println("------------------------------------------------------");
        final SomeFunction function = new StandaloneClass();
        function.func();
        System.out.println("------------------------------------------------------");
    }
}

class StandaloneClass implements SomeFunction {
    @Override
    public void func() {
        System.out.println("StandaloneClass: " + this.getClass().getName());
        StacktracePrinter.print();
    }
}

class StacktracePrinter {
    public static void print() {
        final StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        Stream.of(stacktrace).
                forEach(System.out::println);
    }
}