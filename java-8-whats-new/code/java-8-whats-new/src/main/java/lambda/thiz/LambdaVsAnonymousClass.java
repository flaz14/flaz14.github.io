package lambda.thiz;

import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class LambdaVsAnonymousClass {
    public static void main(String args[]) {
        final SomeClass someObject = new SomeClass();

        //someObject.doWorkWithLambda();
        //someObject.doWorkWithAnonymousInnerClass();
        //someObject.doWorkWithStandaloneClass();

        //someObject.doWorkWithLambdaInSeparateThread();
        //someObject.doWorkWithAnonymousClassInSeparateThread();
        someObject.doWorkWithStandaloneClassInSeparateThread();
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

    public void doWorkWithLambdaInSeparateThread() {
        Executors.newFixedThreadPool(1).submit(
                () -> {
                    System.out.println("!!! doWorkWithLambdaInSeparateThread()");
                    try {
                        throw new StubException("Some error has been occurred.");
                    } catch (StubException thrownManually) {
                        thrownManually.printStackTrace();
                    }
                }
        );
    }

    public void doWorkWithAnonymousClassInSeparateThread() {
        Executors.newFixedThreadPool(1).submit(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("!!! doWorkWithAnonymousClassInSeparateThread()");
                        try {
                            throw new StubException("Some error has been occurred.");
                        } catch (StubException thrownManually) {
                            thrownManually.printStackTrace();
                        }
                    }
                }
        );
    }

    public void doWorkWithStandaloneClassInSeparateThread() {
        Executors.newFixedThreadPool(1).submit(new StandaloneThreadDefinition());
    }
}

class StandaloneClass implements SomeFunction {
    @Override
    public void func() {
        System.out.println("StandaloneClass: " + this.getClass().getName());
        StacktracePrinter.print();
    }
}

class StandaloneThreadDefinition implements Runnable {
    @Override
    public void run() {
        System.out.println("!!! StandaloneThreadDefinition");
        try {
            throw new StubException("Some error has been occurred.");
        } catch (StubException thrownManually) {
            thrownManually.printStackTrace();
        }
    }
}

class StacktracePrinter {
    public static void print() {
        final StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        Stream.of(stacktrace).
                forEach(System.out::println);
    }
}

class StubException extends Exception {
    public StubException(String s) {
        super(s);
    }
}