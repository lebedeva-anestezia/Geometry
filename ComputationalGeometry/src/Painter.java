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

public class Painter extends Canvas {
	private static final long serialVersionUID = 1L;
	private int lastX, lastY;
	private int ex, ey;
	private boolean isLine = false, ready = false,
			result = false;
	SegmentTree tree;
	Segment query;
	List<Segment> segments = new ArrayList<Segment>();
	List<Segment> ans;

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
						segments.add(new Segment(lastX, lastY, ex,
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
				if (e.getKeyChar() == '\n') {
					tree = new SegmentTree(segments);
					ready = true;
				}
			}
		});
	}
	
	public void compute() {
		query = new Segment(lastX, lastY, ex, ey);
		ans = tree.query(query);
		result = true;
		repaint();
	}

	public void update(Graphics g) {
		if (!result) {
			g.drawLine(lastX, lastY, ex, ey);
		} else {
			g.clearRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			for (Segment seg : segments) {
				g.drawLine(seg.x1, seg.y1, seg.x2, seg.y2);
			}
			g.setColor(Color.GREEN);
			g.drawLine(query.x1, query.y1, query.x2, query.y2);
			g.setColor(Color.RED);
			for (Segment seg : ans) {
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