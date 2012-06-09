import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class SegmentTree {

	private Tree tree;
	
	public SegmentTree() {
	}
	
	public SegmentTree(List<Segment> s) {
		tree = build(s);
	}

	static class Node {
		Node leftChild, rightChilrden;
		Interval interval;
		CanonicalTree canonicalSubset;

		public Node(Interval interval) {
			this.interval = interval;
			this.canonicalSubset = new CanonicalTree(interval.left,
					interval.right);
		}

		public Node(Node left, Node right) {
			this.interval = new Interval(left.interval.left,
					right.interval.right);
			this.leftChild = left;
			this.rightChilrden = right;
			this.canonicalSubset = new CanonicalTree(interval.left,
					interval.right);
		}

		public void walk() {
			this.canonicalSubset.build();
			if (leftChild != null)
				leftChild.walk();
			if (rightChilrden != null)
				rightChilrden.walk();
		}

		@Override
		public String toString() {
			return interval.left + " " + interval.right;
		}
	}

	private static class Tree {
		Node root;

		public Tree(List<Node> leaves) {
			int n = leaves.size();
			int k = (Integer.highestOneBit(n) << 1) - n;
			Queue<Node> queue = new ArrayDeque<Node>();
			for (int i = 0; i < n - k; i += 2) {
				queue.add(new Node(leaves.get(i), leaves.get(i + 1)));
			}
			for (int i = n - k; i < n; i++) {
				queue.add(leaves.get(i));
			}
			while (queue.size() > 1) {
				queue.add(new Node(queue.poll(), queue.poll()));
			}
			root = queue.poll();
		}


		public void insert(Node node, Segment segment) {
			if (node.interval.left >= segment.x1
					&& node.interval.right <= segment.x2) {
				node.canonicalSubset.add(segment);
				return;
			}
			if (node.leftChild != null
					&& Util.overlaps(node.leftChild.interval, new Interval(segment))) {
				insert(node.leftChild, segment);
			}
			if (node.rightChilrden != null
					&& Util.overlaps(node.rightChilrden.interval, new Interval(
							segment))) {
				insert(node.rightChilrden, segment);
			}
		}


		public void query(Node node, Segment q, List<Segment> segments) {
			node.canonicalSubset.query(q, segments);
			if (node.leftChild != null) {
				if (q.x1 <= node.leftChild.interval.right) {
					query(node.leftChild, q, segments);
				}
			}
			if (node.rightChilrden != null) {
				if (q.x1 >= node.rightChilrden.interval.left) {
					query(node.rightChilrden, q, segments);
				}
			}
		}
	}

	public List<Segment> query(Segment q) {
		List<Segment> answer = new ArrayList<Segment>();
		tree.query(tree.root, q, answer);
		return answer;
	}

	private Tree build(List<Segment> segments) {
		List<Node> leaves = new ArrayList<Node>();
		List<Integer> endpoints = new ArrayList<Integer>();
		for (Segment seg : segments) {
			endpoints.add(seg.x1);
			endpoints.add(seg.x2);
		}
		int[] points = new int[endpoints.size()];
		for (int i = 0; i < endpoints.size(); i++) {
			points[i] = endpoints.get(i);
		}
		Arrays.sort(points);
		leaves.add(new Node(new Interval(Integer.MIN_VALUE, points[0])));
		leaves.add(new Node(new Interval(points[0], points[0])));
		for (int i = 1; i < points.length; i++) {
			leaves.add(new Node(new Interval(points[i - 1], points[i])));
			leaves.add(new Node(new Interval(points[i], points[i])));
		}
		leaves.add(new Node(new Interval(points[points.length - 1],
				Integer.MAX_VALUE)));
		Tree tree = new Tree(leaves);
		for (Segment seg : segments) {
			tree.insert(tree.root, seg);
		}
		tree.root.walk();
		return tree;
	}

	static SegmentTree read(String fileName) {
		try {
			Scanner sc = new Scanner(new File(fileName));
			List<Segment> segments = new ArrayList<Segment>();
			while (sc.hasNext()) {
				segments.add(new Segment(sc.nextInt(), sc.nextInt(), sc
						.nextInt(), sc.nextInt()));
			}
			sc.close();
			return new SegmentTree(segments);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		SegmentTree segmentTree = read("input.txt");
		Segment query = new Segment(186, 107, 186, 185);
		List<Segment> ans = segmentTree.query(query);
		for (Segment seg : ans) {
			System.err.println(seg);
		}
	}
}
