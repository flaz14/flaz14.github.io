public class SupressedExceptionTest {
	public static void main(String ignored[]) {
		try(MyResource resource = new MyResource()) {
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