
public class TestClass {

	public static void main(String args[]) {
		final CustomLRU<Integer, String> cache = new CustomLRU<Integer, String>(100);
		/* cache.put(1, "asd"); */

		for (int i = 0; i < 100; i++) {
			Thread t2 = new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						cache.put(i, (i) + "");
					}
				}
			});
			t2.start();
			Thread t1 = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						String x = cache.get(i);
						//System.out.println(x == null ? "null" : x);
					}
				}
			});
			t1.start();
		}
	}

}
