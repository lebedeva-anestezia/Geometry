import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

public class Graph {
	NavigableSet<Segment> tree = new TreeSet<Segment>(new SegmentComparator());
	HashMap<Segment, Set<Segment>> graph = new HashMap<Segment, Set<Segment>>();
	List<Segment> order = new ArrayList<Segment>();
	HashMap<Segment, Boolean> color = new HashMap<Segment, Boolean>();

	public Graph() {
	}

	private void dfs(Segment seg) {
		if (color.get(seg)) 
			return;
		color.put(seg, true);
		Iterator<Segment> iter = graph.get(seg).iterator();
		while (iter.hasNext()) {
			dfs(iter.next());
		}
		order.add(seg);
	}

	public List<Segment> buildGraph(List<Endpoint> points) {
		for (Endpoint p : points) {
			if (p.isLeft) {
				tree.add(p.segment);
				graph.put(p.segment, new HashSet<Segment>());
				Segment prev = tree.lower(p.segment);
				if (prev != null) {
					graph.get(p.segment).add(prev);
				}
				Segment next = tree.higher(p.segment);
				if (next != null) {
					graph.get(next).add(p.segment);
				}
				if (next != null && prev != null) {
					graph.get(next).remove(prev);
				}
			} else {
				tree.remove(p.segment);
			}
		}
		for (Segment seg : graph.keySet()) {
			color.put(seg, false);
		}
		for (Segment seg : graph.keySet()) {
			dfs(seg);
		}
		return order;
	}
}
