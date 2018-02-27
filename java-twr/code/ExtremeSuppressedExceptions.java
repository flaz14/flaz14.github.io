public class ExtremeSuppressedExceptions {
	public static void main(String ignored[]) {
		try ( MyResource resource = new MyResource() ) {
			System.out.println(">>> main()");
			throw new IllegalStateException("Exception from main()");
		}
	}
}

class MyResource implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> MyResource.close()");
		try {
			System.out.println(">>> try-block from MyResource");
			throw new IllegalStateException("Exception from try-block from MyResource");
		}
		finally {
			System.out.println(">>> finally-block from MyResource");
			throw new IllegalStateException("Exception from finally-block from MyResource");
		}
	}
}