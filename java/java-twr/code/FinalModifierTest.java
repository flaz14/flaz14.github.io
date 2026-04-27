public class FinalModifierTest {
	public static void main(String ignored[]) {
		try( final MyAutoCloseable my = new MyAutoCloseable() ) {}
		
		// Compile-time error: auto-closeable resource my may not be assigned
		/* 
		try( MyAutoCloseable my = new MyAutoCloseable() ) {
			my = null;
		}
		*/
	}
}

class MyAutoCloseable implements AutoCloseable {
	@Override
	public void close() {}
}