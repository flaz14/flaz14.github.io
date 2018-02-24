public class PolymorphismTest {
	public static void main(String ignored[]) {
		try (
			SomeAutoCloseable resource = new SomeAutoCloseable() {
				@Override
				public void close() {
					System.out.println(">>> Anonymous close()");
				}
			}
		) {}
		
		System.out.println("---------------------");
		
		try ( SomeAutoCloseable resource = new StandaloneCloseAble() ) {}
		
		System.out.println("---------------------");
		
		try ( InterfaceForAutoCloseable tricky = new TrickyAutoCloseable() ) {}
	}
}

class SomeAutoCloseable implements AutoCloseable {
	@Override
	public void close() { System.out.println(">>> Other.close()"); }
}

class StandaloneCloseAble extends SomeAutoCloseable {
	@Override
	public void close() { System.out.println(">>> Standalone close()"); }
}

interface InterfaceForAutoCloseable extends AutoCloseable { 
	void close();
}

class TrickyAutoCloseable implements InterfaceForAutoCloseable {
	@Override
	public void close() { System.out.println(">>> TrickyAutoCloseable.close()"); }
}