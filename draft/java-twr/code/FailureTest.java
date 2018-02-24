public class FailureTest {
	public static void main(String ignored[]) {
		try( 
			Good good = new Good(
				new Bad()
			) 
		) {}
		
		System.out.println("---------------------");
		
		try (
			Bad bad = new Bad();
			Good good = new Good(bad);
		) {}
	}
}

class Good implements AutoCloseable {
	
	public Good(Bad bad) {
	}
	
	@Override
	public void close() {
		System.out.println(">>> Good.close()");
	}
}

class Bad implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> Bad.close()");
		throw new IllegalStateException("Something went wrong...");
	}
}
