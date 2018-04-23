import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;
import java.util.TreeSet;
import java.io.IOException;

public class CustomPermissions {
	public static void main(String args[]) throws IOException {
		Path target = Paths.get("test.txt");
		Set<PosixFilePermission> desiredPermissions = PosixFilePermissions.fromString("rw-rw----");
		FileAttribute<Set<PosixFilePermission>> attribute = PosixFilePermissions.asFileAttribute(desiredPermissions);
		Files.createFile(target, attribute); 
		Set<PosixFilePermission> actualPermissions = Files.getFileAttributeView(target, PosixFileAttributeView.class).
			readAttributes().
			permissions();
		System.out.println(new TreeSet<PosixFilePermission>(actualPermissions));
	}
}