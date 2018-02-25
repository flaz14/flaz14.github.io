public class OrderingTest {
	public static void main(String args[]) {
		System.out.println("--- First - Second");
		try ( 
			First first = new First();
			Second second = new Second();
		) {}
		
		System.out.println("--- Second - First");
		try (
			Second second = new Second();
			First first = new First();
		) {}
		
		System.out.println("--- variable for First is missed");
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