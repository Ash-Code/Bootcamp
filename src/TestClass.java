
public class TestClass {

	public static void main(String args[]) {
		final CustomLRU<Integer, String> cache = new CustomLRU<Integer,String>(100);
	cache.put(1, "asd");
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				
				for (int i = 0; i < 10; i++) {
					cache.put(i, i + "");
				}

			}

		});
		t1.start();
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
			
				for (int i = 0; i < 10; i++) {
					cache.put(1000 - i, (1000 - i) + "");
				}

			}

		});
		t2.start();

	}

}
