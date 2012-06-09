
public class Segment {
	public final int x1, y1, x2, y2;

	public Segment(int x1, int y1, int x2, int y2) {
		if (x1 > x2) {
			this.x1 = x2;
			this.y1 = y2;
			this.x2 = x1;
			this.y2 = y1;
		} else {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}

	@Override
	public String toString() {
		return Integer.valueOf(x1).toString() + " "
				+ Integer.valueOf(y1).toString() + " "
				+ Integer.valueOf(x2).toString() + " "
				+ Integer.valueOf(y2).toString();
	}
}