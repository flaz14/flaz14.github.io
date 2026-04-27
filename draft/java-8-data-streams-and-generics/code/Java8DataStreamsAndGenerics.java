import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;

public class Java8DataStreamsAndGenerics {
	public static void main(String args[]) {
		List<Fruit> apples = Stream.of(1, 2, 3).
		map(dummy -> new Bucket()).
		map(Bucket::apple).
		collect(Collectors.toList());
	}
	
	private static List<Apple> applesOf() {
		return null;
	}
}

interface Fruit {}

interface Apple extends Fruit {}

class Bucket implements Apple {
	public Apple apple() {
		return this;
	}
}