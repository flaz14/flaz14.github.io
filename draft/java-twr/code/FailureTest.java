public class FailureTest {
	public static void main(String ignored[]) {
		try( 
			Good good = new Good("one");
			Bad bad = new Bad(
				new Good("two")
			)
		) {}
	}
}

class Good implements AutoCloseable {
	private final String id;
	
	public Good(String id) {
		System.out.println(">>> Good [" + id + "] <init>");
		this.id = id; 
	}
	
	@Override
	public void close() { System.out.println(">>> Good [" + id + "] close()"); }
}

class Bad implements AutoCloseable {
	public Bad(Good good) {
		System.out.println(">>> Bad.<init>");
		throw new IllegalStateException("Something went wrong...");		
	}
	
	@Override
	public void close() { System.out.println(">>> Bad.close()"); }
}