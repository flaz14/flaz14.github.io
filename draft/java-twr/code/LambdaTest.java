public class LambdaTest {
	public static void main(String ignored[]) {
		try (
			MyAutoCloseable resource = () -> {
				System.out.println(">>> Lambda #1 close()");
			}
		) {}
		
		System.out.println("---------------------");
		
		try (
			MyAutoCloseable resource = () -> {
				System.out.println(">>> Lambda #2 close()");
				System.out.println("Stacktrace:");
				throw new IllegalStateException("Just for illustration.");
			}
		) {}
	}
}

interface MyAutoCloseable extends AutoCloseable {
	@Override
	void close();
}