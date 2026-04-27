public class FactorialLoop {
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
		int result = 1;
		for(int i = 1; i <=n; i++) {
			result *= i;
		}
		return result;
	}
}

