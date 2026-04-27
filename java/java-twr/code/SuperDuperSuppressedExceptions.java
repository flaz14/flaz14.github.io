public class SuperDuperSuppressedExceptions {
	public static void main(String ignored[]) {
		try ( MyResource resource = new MyResource() ) {
			System.out.println(">>> try-block");
			throw new IllegalStateException("Exception from try-block");
		} finally {
			System.out.println(">>> finally-block");
			throw new IllegalStateException("Exception from finally-block");
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