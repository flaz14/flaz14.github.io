public class OrderingTest {
	public static void main(String args[]) {
		try ( 
			First first = new First();
			Second second = new Second();
		) {}
		
		System.out.println("---------------------");
		
		try (
			Second second = new Second();
			First first = new First();
		) {}	
	}
}

class First implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> First.close()");
	}
}

class Second implements AutoCloseable {
	@Override
	public void close() {
		System.out.println(">>> Second.close()");
	}
}