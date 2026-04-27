package io.github.flaz14;

//import io.github.flaz14.
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import java.util.HashSet;
import java.util.Arrays;
//import static java.util.Arrays.asList;

public abstract class IntegerArraysIntersectionCommonTest {
	
	protected final IntegerArraysIntersection implementation;
	
	protected IntegerArraysIntersectionCommonTest(final IntegerArraysIntersection implementation) {
		this.implementation = implementation;
	}
	
	@Test
	public void test() {
		//assertThat()
	}
	
	//protected static HashSet<Integer> asSet(final int[] array) {
		//Arrays.<Integer>asList(array);
		//return null;
	//}
	
	
}


interface A {
	<T> void makeA(T t);
}

class B implements A {
	@Override
	public <T> void makeA(T t) {}
}