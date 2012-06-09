
public class Util {

	public static boolean overlaps(Interval int1, Interval int2) {
		if ((int1.left <= int2.left) && (int1.right >= int2.left))
			return true;
		if ((int1.left <= int2.right) && (int1.right >= int2.right))
			return true;
		if ((int2.left <= int1.left) && (int2.right >= int1.left))
			return true;
		return false;
	}

	public static long vectorProd(long x1, long y1, long x2, long y2, long x3,
			long y3) {
		return (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
	}

	public static boolean intersection(Segment seg1, Segment seg2) {
		if (!overlaps(
				new Interval(Math.min(seg1.y1, seg1.y2), Math.max(seg1.y1,
						seg1.y2)), new Interval(Math.min(seg2.y1, seg2.y2),
						Math.max(seg2.y1, seg2.y2))))
			return false;
		if (vectorProd(seg1.x1, seg1.y1, seg1.x2, seg1.y2, seg2.x1, seg2.y1)
				* vectorProd(seg1.x1, seg1.y1, seg1.x2, seg1.y2, seg2.x2,
						seg2.y2) > 0) {
			return false;
		}
		if (vectorProd(seg2.x1, seg2.y1, seg2.x2, seg2.y2, seg1.x1, seg1.y1)
				* vectorProd(seg2.x1, seg2.y1, seg2.x2, seg2.y2, seg1.x2,
						seg1.y2) > 0) {
			return false;
		}
		return true;
	}

}
