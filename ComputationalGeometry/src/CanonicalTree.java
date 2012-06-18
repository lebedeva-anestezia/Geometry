import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CanonicalTree {
	private List<Segment> list;
	private int left, right;
	private Node root;
	Node[] references;
	
	private static class Node {
		Node leftChild, rightChild, next, prev;
		Segment value;
		
		public Node(Segment v) {
			this.value = v;
		}
	}

	public CanonicalTree(int left, int right) {
		list = new ArrayList<Segment>();
		this.left = left;
		this.right = right;
	}

	public void add(Segment seg) {
		list.add(seg);
	}

	public void build() {
		if (list.isEmpty())
			return;
		references = new Node[list.size()];
		root = build(0, list.size() - 1);
		for (int i = 1; i < list.size(); i++) {
			references[i].prev = references[i - 1];
			references[i - 1].next = references[i];
		}
	}

	private Node build(int left, int right) {
		if (left > right)
			return null;
		int index = (left + right + 1) / 2;
		Node node = new Node(list.get(index));
		references[index] = node;
		node.leftChild = build(left, index - 1);
		node.rightChild = build(index + 1, right);
		return node;
	}

	public void query(Segment query, List<Segment> answer) {
		if (list.size() == 0) 
			return;
		int bot = Math.min(query.y1, query.y2);
		int t = Math.max(query.y1, query.y2);
		Segment bottom = new Segment(left, bot, right, bot);
		Segment top = new Segment(left, t, right, t);
		Comparator<Segment> comparator = new SegmentComparator(query.x1);
		Node node = root;
		boolean fl = false;
		while (!fl) {
			if (node == null)
				break;
			if (comparator.compare(node.value, top) > 0) {
				node = node.leftChild;
			} else {
				if (comparator.compare(node.value, bottom) < 0) {
					node = node.rightChild;
				} else {
					fl = true;
				}
			}
		}
		if (!fl) 
			return;
		Node prev = node;
		while (prev != null && Util.intersection(prev.value, query)) {
			answer.add(prev.value);
			prev = prev.prev;
		}
		Node next = node.next;
		while (next != null && Util.intersection(next.value, query)) {
			answer.add(next.value);
			next = next.next;
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(list);
	}
}