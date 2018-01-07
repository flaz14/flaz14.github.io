public class TestLambdaStacktraces {
	public static void main(String ignored[]) {
		final SampleClass sampleObject = new SampleClass();
		sampleObject.someInstanceMethod();
		
		System.out.println("---   Inside main method of the application - instanceFieldLambda      ----");
		sampleObject.instanceFieldLambda.print();
		System.out.println("---------------------------------------------------------------------------");
		
		System.out.println("---   Inside main method of the application - staticFieldLambda        ----");		
		sampleObject.staticFieldLambda.print();
		System.out.println("---------------------------------------------------------------------------");
		
		System.out.println("---   Inside main method of the application - localVariableLambda      ----");
		final StackTracePrinter localVariableLambda = () -> {
			StackTracePrinter.printNicely();
		};
		localVariableLambda.print();
		System.out.println("---------------------------------------------------------------------------");
	}
}

class SampleClass {
	public final StackTracePrinter instanceFieldLambda = () -> {
		StackTracePrinter.printNicely();
	};
	
	public static final StackTracePrinter staticFieldLambda = () -> {
		StackTracePrinter.printNicely();
	};
	
	public SampleClass() {
		System.out.println("---   Inside constructor of the enclosing class - instanceFieldLambda   ---");
		instanceFieldLambda.print();
		System.out.println("---------------------------------------------------------------------------");
		
		System.out.println("---   Inside constructor of the enclosing class - staticFieldLambda     ---");
		staticFieldLambda.print();
		System.out.println("---------------------------------------------------------------------------");
		
		System.out.println("---   Inside constructor of the enclosing class - localVariableLambda   ---");
		final StackTracePrinter localVariableLambda = () -> { 
			StackTracePrinter.printNicely();
		};
		localVariableLambda.print();
		System.out.println("---------------------------------------------------------------------------");
	}
	
	public void someInstanceMethod() {
		System.out.println("---   Inside instance method of the enclosing class - instanceFieldLambda -");
		instanceFieldLambda.print();
		System.out.println("---------------------------------------------------------------------------");
		
		System.out.println("---   Inside instance method of the enclosing class - staticFieldLambda ---");
		staticFieldLambda.print();
		System.out.println("---------------------------------------------------------------------------");
		
		System.out.println("---   Inside instance method of the enclosing class - localVariableLambda -");
		final StackTracePrinter localVariableLambda = () -> { 
			StackTracePrinter.printNicely();
		};
		localVariableLambda.print();
		System.out.println("---------------------------------------------------------------------------");
	}
	
	public static void someStaticMethod() {
		System.out.println("---   Inside static method of the enclosing class - staticFieldLambda   ---");
		staticFieldLambda.print();
		System.out.println("---------------------------------------------------------------------------");
		
		System.out.println("---   Inside static method of the enclosing class - localVariableLambda ---");
		final StackTracePrinter localVariableLambda = () -> { 
			StackTracePrinter.printNicely();
		};
		localVariableLambda.print();
		System.out.println("---------------------------------------------------------------------------");	
	}
}

@FunctionalInterface
interface StackTracePrinter {
	void print();
	
	static void printNicely() {
		final StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		java.util.stream.Stream.
			of(stacktrace).
			forEach(System.out::println);
	}
}