public class Interval {
	int left, right;

	public Interval(int left, int right) {
		this.left = left;
		this.right = right;
	}

	public Interval(Segment segment) {
		this.left = segment.x1;
		this.right = segment.x2;
	}
	
	public boolean isPoint() {
		return left == right;
	}
}