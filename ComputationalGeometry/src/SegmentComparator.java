import java.math.BigInteger;
import java.util.Comparator;

public class SegmentComparator implements Comparator<Segment> {
	private int vertical;
	private boolean isQuery;

	public SegmentComparator() {
		isQuery = false;
	}

	public SegmentComparator(int vertical) {
		this.vertical = vertical;
		isQuery = true;
	}

	@Override
	public int compare(Segment o1, Segment o2) {
		long a1 = (long)o1.y2 - o1.y1;
		long b1 = (long)o1.x1 - o1.x2;
		if (b1 == 0)
			return Integer.valueOf(o1.y1).compareTo(o2.y1);
		BigInteger bigA1 = BigInteger.valueOf(a1);
		BigInteger bigB1 = BigInteger.valueOf(b1);
		BigInteger bigC1 = BigInteger.valueOf(-a1 * o1.x1).add(BigInteger.valueOf(-b1 * o1.y1));
		if (b1 < 0) {
			bigA1 = bigA1.negate();
			bigB1 = bigB1.negate();
			bigC1 = bigC1.negate();
		}
		long a2 = (long)o2.y2 - o2.y1;
		long b2 = (long)o2.x1 - o2.x2;
		BigInteger bigA2 = BigInteger.valueOf(a2);
		BigInteger bigB2 = BigInteger.valueOf(b2);
		BigInteger bigC2 = BigInteger.valueOf(-a2 * o2.x1).add(BigInteger.valueOf(-b2 * o2.y1));
		if (b2 < 0) {
			bigA2 = bigA2.negate();
			bigB2 = bigB2.negate();
			bigC2 = bigC2.negate();
		}
		if (!isQuery) {
			vertical = 0;
			if (o1.x1 >= o2.x1) {
				vertical = o1.x1;
			} else {
				vertical = o2.x1;
			}
		}
		BigInteger bigVertical = BigInteger.valueOf(vertical);
		BigInteger bigY1 = (bigA1.multiply(bigVertical).add(bigC1)).multiply(bigB2);
		BigInteger bigY2 = (bigA2.multiply(bigVertical).add(bigC2)).multiply(bigB1);
		return bigY2.compareTo(bigY1);
	}
}