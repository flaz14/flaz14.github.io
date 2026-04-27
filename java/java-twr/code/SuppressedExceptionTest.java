public class SuppressedExceptionTest {
	public static void main(String ignored[]) {
		try(MyResource resource = new MyResource()) {
			System.out.println(">>> try-block");
			throw new IllegalStateException("Exception from try-block");
		} finally {
			System.out.println(">>> finally-block");
		}
	}
}

class MyResource implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> MyResource.close()");
		throw new IllegalStateException("Exception from MyResource.close()");
	}
}