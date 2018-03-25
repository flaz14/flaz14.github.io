public class FactorialTwr {
	public static void main(String ignored[]) {
		System.out.println(
			String.format("%3s: %15s\n", "n", "factorial(n)") + 
			String.format("%3d: %15d\n", 1, factorial(1)) + 
			String.format("%3d: %15d\n", 2, factorial(2)) +
			String.format("%3d: %15d\n", 3, factorial(3)) + 
			String.format("%3d: %15d\n", 4, factorial(4)) + 
			String.format("%3d: %15d\n", 5, factorial(5)) + 
			""
		);
	}
	
	private static int factorial(int n) {
		int[] resultPlaceholder = new int[] {1};
		try (FactorialAutoCloseable calculator = new FactorialAutoCloseable(n, resultPlaceholder)) { }
		return resultPlaceholder[0];
	}
	
	private static class FactorialAutoCloseable implements AutoCloseable {
		private int n;
		private int[] resultPlaceholder;
		
		public FactorialAutoCloseable(int n, int[] resultPlaceholder) {
			this.n = n;
			this.resultPlaceholder = resultPlaceholder;
		}
		
		@Override
		public void close() {
			if (n == 1) return;
			resultPlaceholder[0] *= n; 
			try(FactorialAutoCloseable calculator = new FactorialAutoCloseable(n - 1, resultPlaceholder)) { } 
		}
	}
}