public class VariablesTest {
	public static void main(String ignored[]) {
		try( MyAutoCloseable resource = new MyAutoCloseable() ) { }
		
		//try( AutoCloseable resource = new MyAutoCloseable() ) { }
	
		//try( new MyAutoCloseable() ) { }
	}
}

class MyAutoCloseable implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> close()"); 
	}
}