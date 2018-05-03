import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;

public class Java8DataStreamsAndGenerics {
	public static void main(String args[]) {
		List<Banana> apples = Stream.of("Earth", "Mars", "Planet X").
		map(origin -> new Fruit(origin)).
		map(Bucket::apple).
		collect(Collectors.toList());
	}
}

interface Fruit {}

interface Apple extends Fruit {}

//interface Banana {}

//interface Orange {}

class Bucket implements Apple {
	public Apple apple() {
		return this;
	}
}