public class CrazyTwr {
	public static void main(String ignored[]) {
		System.out.println(">>> main()");
		try ( MyResource resource = new MyResource() ) {
			System.out.println(">>> try-block from main()");
			throw new IllegalStateException("Exception from main()");
		} finally {
			System.out.println(">>> finally-block from main()");
		}
	}
}

class MyResource implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> MyResource.close()");
		try ( MyResource resource = new MyResource() ) {
			System.out.println(">>> try-block from MyResource");
			throw new IllegalStateException("Exception from MyResource.close()");
		} finally {
			System.out.println(">>> finally-block from MyResource");
		}
	}
}