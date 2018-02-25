import java.io.InputStream;
import java.io.IOException;

public class ExceptionTest {
	public static void main(String ignored[]) throws IOException {
		try(InputStream i = getNullStream()) {
			i.available();
		}
	}
	
	private static InputStream getNullStream() { 
		return null;
	}
}