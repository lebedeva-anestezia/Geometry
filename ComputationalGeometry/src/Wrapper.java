import java.util.List;

public class Wrapper {
	private SegmentTree segmentTree;
	private List<Segment> queries;
	
	public Wrapper(SegmentTree t, List<Segment> q) {
		segmentTree = t;
		queries = q;
	}
	
	public SegmentTree getSegmentTree() {
		return segmentTree;
	}
	
	public List<Segment> getQueries() {
		return queries;
	}
}
