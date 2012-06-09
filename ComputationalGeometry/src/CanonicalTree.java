import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CanonicalTree {
	List<Segment> list;
	int left, right;
	Segment[] tree;

	public CanonicalTree(int left, int right) {
		list = new ArrayList<Segment>();
		this.left = left;
		this.right = right;
	}

	public void add(Segment seg) {
		list.add(seg);
	}

	public void build() {
		Collections.sort(list, new SegmentComparator(left));
		tree = new Segment[list.size()];
		build(0, 0, tree.length - 1);
	}

	private void build(int rootIndex, int left, int right) {
		if (left > right)
			return;
		int index = (left + right + 1) / 2;
		tree[rootIndex] = list.get(index);
		build(2 * rootIndex + 1, left, index - 1);
		build(2 * rootIndex + 2, index + 1, right);
	}

	public void query(Segment query, List<Segment> answer) {
		if (list.size() == 0) 
			return;
		int bot = Math.min(query.y1, query.y2);
		int t = Math.max(query.y1, query.y2);
		Segment bottom = new Segment(left, bot, right, bot);
		Segment top = new Segment(left, t, right, t);
		Comparator<Segment> comparator = new SegmentComparator(query.x1);
		int indNode = 0;
		boolean fl = false;
		while (!fl) {
			if (indNode >= tree.length)
				break;
			if (comparator.compare(tree[indNode], top) > 0) {
				indNode <<= 1;
				indNode += 1;
			} else {
				if (comparator.compare(tree[indNode], bottom) < 0) {
					indNode <<= 1;
					indNode += 2;
				} else {
					fl = true;
				}
			}
		}
		if (!fl) 
			return;
		int prev = indNode;
		while (prev != -1 && Util.intersection(tree[prev], query)) {
			answer.add(tree[prev]);
			prev = prev(prev);
		}
		int next = next(indNode);
		while (next != -1 && Util.intersection(tree[next], query)) {
			answer.add(tree[next]);
			next = next(next);
		}
	}

	private boolean hasLeft(int ind) {
		ind <<= 1;
		ind++;
		return ind < tree.length;
	}

	private boolean hasRight(int ind) {
		ind <<= 1;
		ind += 2;
		return ind < tree.length;
	}

	private int prev(int ind) {
		if (tree.length == 1)
			return -1;
		int res = ind;
		if (hasLeft(res)) {
			res <<= 1;
			res++;
			while (hasRight(res)) {
				res <<= 1;
				res += 2;
			}
			return res;
		}
		while ((res & 1) == 1) {
			res >>= 1;
		}
		if (res == 0) 
			return -1;
		res--;
		res >>= 1;
		return res;
	}
	
	private int next(int ind) {
		if (tree.length == 1)
			return -1;
		int res = ind;
		if (hasRight(res)) {
			res <<= 1;
			res += 2;
			while (hasLeft(res)) {
				res <<= 1;
				res++;
			}
			return res;
		}
		while (res != 0 && (res & 1) == 0) {
			res--;
			res >>= 1;
		}
		if (res == 0) 
			return -1;
		res >>= 1;
		return res;
	}
}