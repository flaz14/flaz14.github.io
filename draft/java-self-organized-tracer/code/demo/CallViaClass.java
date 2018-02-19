import io.github.flaz14.publicapi.Traceable;

public class CallViaClass {
	public static void main(String ignoredArgs[]) throws ClassNotFoundException {
		System.out.println("!!!CallViaClass test!!!");
		Class.forName("io.github.flaz14.Loader");
		MyTraceable myTraceable = new MyTraceable();
		myTraceable.processSomething("bank");
		myTraceable.getSomething();
		myTraceable.processSomething("account1", "account2");
		Traceable traceable = myTraceable;
		traceable.getSomething();
	}
}

class MyTraceable implements Traceable<String> {
	@Override
	public void processSomething(String s) {
		System.out.println(">>> processSomething()");
	}
	
	public void processSomething(String s1, String s2) {
		System.out.println(">>> processSomething() - NOT overriden, SHOULD NOT be traced.");
	}

	@Override
	public String getSomething() {
		System.out.println(">>> getSomething()");
		return null;
	}
}