import java.util.Comparator;


public class SegmentComparator implements Comparator<Segment> {
	private int vertical;

	public SegmentComparator(int vertical) {
		this.vertical = vertical;
	}

	@Override
	public int compare(Segment o1, Segment o2) {
		long a1 = o1.y2 - o1.y1;
		long b1 = o1.x1 - o1.x2;
		long c1 = -a1 * o1.x1 - b1 * o1.y1;
		if (b1 < 0) {
			a1 *= -1;
			b1 *= -1;
			c1 *= -1;
		}
		long a2 = o2.y2 - o2.y1;
		long b2 = o2.x1 - o2.x2;
		long c2 = -a2 * o2.x1 - b2 * o2.y1;
		if (b2 < 0) {
			a2 *= -1;
			b2 *= -1;
			c2 *= -1;
		}
		if (b1 == 0)
			return Integer.valueOf(o1.y1).compareTo(o2.y1);
		long y1 = -(a1 * vertical + c1) * b2;
		long y2 = -(a2 * vertical + c2) * b1;
		return Long.valueOf(y1).compareTo(y2);
	}

}