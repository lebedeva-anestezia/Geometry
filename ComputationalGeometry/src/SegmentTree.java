import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
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
		
		public void insertAll(Node node, List<Segment> segs) {
			if (segs.isEmpty())
				return;
			List<Segment> left = new ArrayList<Segment>();
			List<Segment> right = new ArrayList<Segment>();
			boolean added = false;
			for (Segment seg : segs) {
				added = false;
				if (node.interval.left >= seg.x1
						&& node.interval.right <= seg.x2) {
					node.canonicalSubset.add(seg);
					added = true;
				}
				if (!added && node.leftChild != null
						&& Util.overlaps(node.leftChild.interval, new Interval(seg))) {
					left.add(seg);
				}
				if (!added && node.rightChilrden != null
						&& Util.overlaps(node.rightChilrden.interval, new Interval(
								seg))) {
					right.add(seg);
				}
			}
			node.canonicalSubset.build();
			insertAll(node.leftChild, left);
			insertAll(node.rightChilrden, right);
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
		List<Endpoint> endpoints = new ArrayList<Endpoint>();
		for (Segment seg : segments) {
			endpoints.add(new Endpoint(seg, true));
			endpoints.add(new Endpoint(seg, false));
		}
		Collections.sort(endpoints);
		Graph graph = new Graph();
		List<Segment> order = graph.buildGraph(endpoints);
		leaves.add(new Node(new Interval(Integer.MIN_VALUE, endpoints.get(0).x)));
		leaves.add(new Node(new Interval(endpoints.get(0).x, endpoints.get(0).x)));
		for (int i = 1; i < endpoints.size(); i++) {
			leaves.add(new Node(new Interval(endpoints.get(i - 1).x, endpoints.get(i).x)));
			leaves.add(new Node(new Interval(endpoints.get(i).x, endpoints.get(i).x)));
		}
		leaves.add(new Node(new Interval(endpoints.get(endpoints.size() - 1).x,
				Integer.MAX_VALUE)));
		Tree tree = new Tree(leaves);
		tree.insertAll(tree.root, order);
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
		Segment query = new Segment(200, 80, 200, 140);
		List<Segment> ans = segmentTree.query(query);
		for (Segment seg : ans) {
			System.err.println(seg);
		}
	}
}
