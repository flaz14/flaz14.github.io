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
		
		System.out.println("---------------------");
		
		try (
			Second second = new Second(
				new First()
			)
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
	public Second() {}
	
	public Second(First first) {}
	
	@Override
	public void close() {
		System.out.println(">>> Second.close()");
	}
}