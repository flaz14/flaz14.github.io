public class PolymorphismTest {
	public static void main(String ignored[]) {
		try (
			SomeAutoCloseable resource = new SomeAutoCloseable() {
				@Override
				public void close() { System.out.println(">>> Anonymous close()"); }
			}
		) {}
		
		try ( SomeAutoCloseable resource = new StandaloneCloseAble() ) {}
		
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
	@Override
	void close();
}

class TrickyAutoCloseable implements InterfaceForAutoCloseable {
	@Override
	public void close() { System.out.println(">>> TrickyAutoCloseable.close()"); }
}