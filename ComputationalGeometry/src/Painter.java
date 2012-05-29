import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Painter extends Canvas {
	private static final long serialVersionUID = 1L;
	private int lastX, lastY;
	private int ex, ey;
	private boolean clear = false, isLine = false, ready = false,
			result = false;
	SegmentTree tree;
	SegmentTree.Segment query;
	List<SegmentTree.Segment> segments = new ArrayList<SegmentTree.Segment>();
	Set<SegmentTree.Segment> ans;

	public Painter() {
		super();
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!isLine) {
					lastX = e.getX();
					lastY = e.getY();
					isLine = true;
				} else {
					ey = e.getY();
					if (!ready) {
						ex = e.getX();
						segments.add(new SegmentTree.Segment(lastX, lastY, ex,
								ey));
						repaint();
					} else {
						ex = lastX;
						compute();
					}
					isLine = false;
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == ' ') {
					clear = true;
					repaint();
				}
				if (e.getKeyChar() == '\n') {
					ready = true;
				}
			}
		});
	}
	
	public void compute() {
		query = new SegmentTree.Segment(lastX, lastY, ex, ey);
		tree = new SegmentTree(segments, query);
		tree.run();
		ans = tree.getAnswer();
		result = true;
		repaint();
	}

	public void update(Graphics g) {
		if (!result) {
			if (clear) {
				g.clearRect(0, 0, getWidth(), getHeight());
				clear = false;
			} else {
				g.drawLine(lastX, lastY, ex, ey);
			}
		} else {
			g.clearRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			for (SegmentTree.Segment seg : segments) {
				g.drawLine(seg.x1, seg.y1, seg.x2, seg.y2);
			}
			g.setColor(Color.GREEN);
			g.drawLine(query.x1, query.y1, query.x2, query.y2);
			g.setColor(Color.RED);
			for (SegmentTree.Segment seg : ans) {
				g.drawLine(seg.x1, seg.y1, seg.x2, seg.y2);
			}
			ans.clear();
		}
	}

	public static void main(String s[]) {
		final Frame f = new Frame("SegmentTree");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				f.dispose();
			}
		});
		f.setSize(500, 500);

		final Canvas c = new Painter();
		f.add(c);

		f.setVisible(true);
	}
}