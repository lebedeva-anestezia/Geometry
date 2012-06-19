import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

public class SegmentTree {

	private Tree tree;

	public SegmentTree() {
	}

	public SegmentTree(List<Segment> s) {
		tree = build(s);
	}

	static class Node {
		Node leftChild, rightChild;
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
			this.rightChild = right;
			this.canonicalSubset = new CanonicalTree(interval.left,
					interval.right);
		}

		public boolean isLeaf() {
			return leftChild == null && rightChild == null;
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
				if (!added
						&& node.leftChild != null
						&& Util.overlaps(node.leftChild.interval, new Interval(
								seg))) {
					left.add(seg);
				}
				if (!added
						&& node.rightChild != null
						&& Util.overlaps(node.rightChild.interval,
								new Interval(seg))) {
					right.add(seg);
				}
			}
			node.canonicalSubset.build();
			insertAll(node.leftChild, left);
			insertAll(node.rightChild, right);
		}

		public void query(Node node, Segment q, List<Segment> segments) {
			if (node.isLeaf() && !node.interval.isPoint() && (q.x1 == node.interval.left || q.x1 == node.interval.right))
				return;
			node.canonicalSubset.query(q, segments);
			if (node.isLeaf())
				return;
			if (q.x1 <= node.leftChild.interval.right) {
				query(node.leftChild, q, segments);
			}
			if (q.x1 >= node.rightChild.interval.left) {
				query(node.rightChild, q, segments);
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
		Set<Integer> setOfEndpoints = new HashSet<Integer>();
		for (Segment seg : segments) {
			endpoints.add(new Endpoint(seg, true));
			endpoints.add(new Endpoint(seg, false));
			setOfEndpoints.add(seg.x1);
			setOfEndpoints.add(seg.x2);
		}
		int[] ends = new int[setOfEndpoints.size()];
		int ind = 0;
		for (Integer point : setOfEndpoints) {
			ends[ind] = point;
			ind++;
		}
		Arrays.sort(ends);
		Collections.sort(endpoints);
		Graph graph = new Graph();
		List<Segment> order = graph.buildGraph(endpoints);
		leaves.add(new Node(new Interval(Integer.MIN_VALUE, ends[0])));
		leaves.add(new Node(new Interval(ends[0], ends[0])));
		for (int i = 1; i < ends.length; i++) {
			leaves.add(new Node(new Interval(ends[i - 1], ends[i])));
			leaves.add(new Node(new Interval(ends[i], ends[i])));
		}
		leaves.add(new Node(new Interval(ends[ends.length - 1], Integer.MAX_VALUE)));
		Tree tree = new Tree(leaves);
		tree.insertAll(tree.root, order);
		return tree;
	}

	static Wrapper read(String fileName) {
		try {
			Scanner sc = new Scanner(new File(fileName));
			List<Segment> segments = new ArrayList<Segment>();
			List<Segment> queries = new ArrayList<Segment>();
			int n = 0;
			while (sc.hasNext()) {
				String s = sc.nextLine();
				if (s.equals(""))
					break;
				StringTokenizer t = new StringTokenizer(s);
				segments.add(new Segment(Integer.valueOf(t.nextToken()),
						Integer.valueOf(t.nextToken()), Integer.valueOf(t
								.nextToken()), Integer.valueOf(t.nextToken()),
						++n));
			}
			while (sc.hasNext()) {
				queries.add(new Segment(sc.nextInt(), sc.nextInt(), sc
						.nextInt(), sc.nextInt()));
			}
			sc.close();
			return new Wrapper(new SegmentTree(segments), queries);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		Wrapper wrap = read("input.txt");
		SegmentTree segmentTree = wrap.getSegmentTree();
		List<Segment> queries = wrap.getQueries();
		try {
			FileWriter out = new FileWriter(new File("output.txt"));
			for (Segment query : queries) {
				List<Segment> ans = segmentTree.query(query);
				Collections.sort(ans);
				for (Segment seg : ans) {
					out.write(seg.getNumber() + " ");
				}
				out.write('\n');
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
