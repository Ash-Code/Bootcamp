import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class CustomLRU<K, V> {
	private class Entry {
		V value;
		K key;
		Node node;

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

	}

	private class Node {
		Node prev;
		Node next;
		Entry entry;

		Node(Entry entry) {
			this.entry = entry;
		}
	}

	private ConcurrentHashMap<K, Entry> map;
	private int numItems;
	private Node head;
	private Node tail;

	public CustomLRU(int numItems) {
		map = new ConcurrentHashMap<>();
		System.out.println("INstantiated");
		this.numItems = numItems;
	}

	public void put(K key, V value) {
		Entry e = new Entry(key, value);
		map.put(e.key, e);
		offer(e);
		purge();
	}

	public V get(K key) {
		if (map.containsKey(key)) {
			Entry e = map.get(key);
			offer(e);
			return e.value;
		}
		return null;
	}

	private void purge() {

		while (map.size() > numItems) {
			if (tail != null) {
				// System.out.println("Tail is not null");
				map.remove(tail.entry.key);
				tail = tail.prev;
				tail.next = null;
			} else {
				System.out.println("Tail is null");
				break;
			}
		}
	}

	private void offer(Entry e) {

		if (head == null || head.entry != e) {
			Node c = e.node;
			Node node = new Node(e);
			AtomicReference<Node> ref = new AtomicReference<Node>(e.node);
			if (ref.compareAndSet(c, node)) {
				addToHead(node);
				if (c != null) {
					removeNode(c);
				}
			}
		}

	}

	private void removeNode(Node c) {
		if (c == tail) {
			tail = tail.next;
			if (tail != null)
				tail.prev = null;
			return;
		}
		if (c.prev != null) {
			c.prev.next = c.next;
		}
		if (c.next != null) {
			c.next.prev = c.prev;
		}
	}

	private synchronized void addToHead(Node n) {
		if (head == null) {
			tail = n;
			head = n;
			n.next = null;
			n.prev = null;
			return;
		}
		n.next = head;
		head.prev = n;
		head = n;
	}
}
