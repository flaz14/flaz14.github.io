import java.util.Scanner;
import java.util.Objects;

public class App {
	private static final String VALID_SERIAL_NUMBER = "qwerty";	
	
	public static void main(String ignoredArgs[]) {
		System.out.println(	"****************************************\n" +
							"* SuperMega Application by flaz14 Inc. *\n" + 
							"****************************************\n" +
							"\n" + 
							"Hello, User!" + 
							"\n" + 
							"Please enter your Serial Number below\n" +
							"and hit ENTER: " + 
							"\n");
		String rawSerialNumber = null;
		try (Scanner keyboard = new Scanner(System.in, "UTF-8")) {
			if (keyboard.hasNextLine())
				rawSerialNumber = keyboard.nextLine();
		}
		if (rawSerialNumber == null) 
			throw new IllegalStateException("Internal error: serialNumber " +
											"should not be null.");
		String serialNumber = rawSerialNumber.trim();
		if ( Objects.equals(serialNumber, VALID_SERIAL_NUMBER) )
			System.out.println(	"\nCongratulations! You've successfully\n" +
								"activated your copy of the application.");
		else 
			System.out.println( "\nSorry, the serial you've entered is invalid.");
	}
}