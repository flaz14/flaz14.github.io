import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.TreeSet;
import java.io.IOException;

public class DefaultPermissions {
	public static void main(String args[]) throws IOException {
		Path target = Paths.get("test.txt");
		Files.createFile(target);
		Set<PosixFilePermission> permissions = Files.getFileAttributeView(target, PosixFileAttributeView.class).
			readAttributes().
			permissions();
		System.out.println(new TreeSet<PosixFilePermission>(permissions));
	}
}