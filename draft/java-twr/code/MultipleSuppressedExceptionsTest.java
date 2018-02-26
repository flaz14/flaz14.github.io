public class MultipleSuppressedExceptionsTest {
	public static void main(String ignored[]) {
		try (
			MyResource apple = new MyResource("apple");
			MyResource banana = new MyResource("banana");
			MyResource orange = new MyResource("orange");
			MyResource cherry = new MyResource("cherry");
		) { 
			System.out.println(">>> try-block");
			throw new IllegalStateException("Exception from try-block");
		} finally {
			System.out.println(">>> finally-block");
		}
	}
}

class MyResource implements AutoCloseable {
	private final String id;
	
	public MyResource(String id) { this.id = id;}
	
	@Override
	public void close() { 
		System.out.println(">>> MyResource [" + id + "] close()"); 
		throw new IllegalStateException("Exception from MyResource [" + id + "]");
	}
}