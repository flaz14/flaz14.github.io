public class LambdaTest {
	public static void main(String ignored[]) {
		try (
			MyAutoCloseable resources = () -> {
				System.out.println(">>> Lambda #1 close()");
			}
		) {}
		
		System.out.println("---------------------");
		
		
	}
}

interface MyAutoCloseable extends AutoCloseable {
	@Override
	void close();
}