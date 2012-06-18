
public class Endpoint implements Comparable<Endpoint> {
	public final int x, y;
	public final boolean isLeft;
	public final Segment segment;
	
	public Endpoint(Segment seg, boolean isLeft) {
		if (isLeft) {
			x = seg.x1;
			y = seg.y1;
		} else {
			x = seg.x2;
			y = seg.y2;
		}
		segment = seg;
		this.isLeft = isLeft;
	}
	
	public int getX() {
		return x;
	}

	@Override
	public int compareTo(Endpoint o) {
		if (x == o.x) {
			if (!(isLeft ^ o.isLeft)) {
				return Integer.valueOf(y).compareTo(o.y);
			}
			return Boolean.valueOf(isLeft).compareTo(o.isLeft);
		}
		return Integer.valueOf(x).compareTo(o.x);
	}
	
	@Override
	public String toString() {
		return String.valueOf(x);
	}
}
