public class SupressedExceptionTest {
	public static void main(String ignored[]) {
		try(MyResource resource = getMyResource()) {
		}
	}
	
	private static MyResource getMyResource() {
		return null;
	}
}

class MyResource implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> MyResource.close()");
	}
}