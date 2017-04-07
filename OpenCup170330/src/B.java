import java.io.*;
import java.util.*;

public class B {
	FastScanner in;
	PrintWriter out;

	class Point {
		double x, y;

		public Point(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return x + " " + y;
		}

	}

	class Line {
		double A, B, C;

		public Line(double a, double b, double c) {
			super();
			A = a;
			B = b;
			C = c;
		}

		@Override
		public String toString() {
			return "Line [A=" + A + ", B=" + B + ", C=" + C + "]";
		}

	}

	double eps = 1e-9;

	ArrayList<Line> tangent(Point A, Point B, double r1, double r2) {
		double vx = B.x - A.x;
		double vy = B.y - A.y;
		double dist = vx * vx + vy * vy;
		ArrayList<Line> res = new ArrayList<B.Line>();

		for (int mul1 = -1; mul1 < 2; mul1 += 2) {
			for (int mul2 = -1; mul2 < 2; mul2 += 2) {
				double d1 = r1 * mul1, d2 = r2 * mul2;

				double r = (d2 - d1);
				double diff = r * r;
				double D = dist - diff;
				if (D < -eps) {
					continue;
				}
				D = Math.sqrt(Math.max(0, D));

				double a = ((d2 - d1) * vx + vy * D) / dist;
				double b = ((d2 - d1) * vy - vx * D) / dist;
				// System.err.println("sq = " + (a * a + b * b));
				double c = r1 * mul1 - a * A.x - b * A.y;
				// System.err.println("!! " + (a * A.x + b * A.y + c) + " " +
				// r1);
				// System.err.println("!! " + (a * B.x + b * B.y + c) + " " +
				// r2);
				res.add(new Line(a, b, c));
			}
		}
		return res;
	}

	Point[] pts;
	int[][] d;
	Line[] lines = new Line[3];

	Point intersect(Line l1, Line l2) {
		double zn = -(l1.A * l2.B - l2.A * l1.B);
		if (Math.abs(zn) < eps) {
			return null;
		}
		double y = l2.C * l1.A - l1.C * l2.A;
		double x = l2.B * l1.C - l1.B * l2.C;
		x /= zn;
		y /= zn;
		// System.err.println(l1.A * x + l1.B * y + l1.C);
		// System.err.println(l2.A * x + l2.B * y + l2.C);
		return new Point(x, y);
	}

	int vectMul(Point a, Point b, Point c) {
		double res = (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y);
		return res > 0 ? 1 : -1;
	}

	boolean more = false;

	void check() {
		Point Y = intersect(lines[0], lines[1]);
		Point Z = intersect(lines[1], lines[2]);
		Point X = intersect(lines[0], lines[2]);
		// System.err.println("check: " + X + " " + Y + " " + Z);
		// System.err.println(lines[0]);
		// System.err.println(lines[1]);
		// System.err.println(lines[2]);
		if (X == null || Y == null || Z == null) {
			return;
		}
		if (vectMul(X, Y, Z) < 0) {
			Point tmp = Y;
			Y = Z;
			Z = tmp;
		} else {
			if (!more) {
				return;
			}
		}
		// System.err.println("!");
		for (int it = 0; it < 2; it++) {
			int sign = Integer.signum(d[it][0]);
			int inside = vectMul(X, Y, pts[it]) > 0
					&& vectMul(Y, Z, pts[it]) > 0 && vectMul(Z, X, pts[it]) > 0 ? 1
					: -1;
			if (sign != inside) {
				return;
			}
		}
		// System.err.println("GOOD");
		out.println(X);
		if (more) {
			out.println(Y);
			out.println(Z);
		} else {
			out.println(Z);
			out.println(Y);
		}
		out.close();
		System.exit(0);
	}

	void go(int pos) {
		if (pos == 3) {
			check();
		} else {
			ArrayList<Line> res = tangent(pts[0], pts[1], Math.abs(d[0][pos]),
					Math.abs(d[1][pos]));
			for (Line l : res) {
				lines[pos] = l;
				go(pos + 1);
			}
		}
	}

	void solve() {
		pts = new Point[2];
		d = new int[2][3];
		for (int i = 0; i < 2; i++) {
			pts[i] = new Point(in.nextInt(), in.nextInt());
			for (int j = 0; j < 3; j++) {
				d[i][j] = in.nextInt();
			}
		}
		go(0);
		more = true;
		//go(0);
		throw new AssertionError();
	}

	void run() {
		try {
			in = new FastScanner(new File("object.in"));
			out = new PrintWriter(new File("object.out"));

			solve();

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	void runIO() {

		in = new FastScanner(System.in);
		out = new PrintWriter(System.out);

		solve();

		out.close();
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner(File f) {
			try {
				br = new BufferedReader(new FileReader(f));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public FastScanner(InputStream f) {
			br = new BufferedReader(new InputStreamReader(f));
		}

		String next() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return null;
				st = new StringTokenizer(s);
			}
			return st.nextToken();
		}

		boolean hasMoreTokens() {
			while (st == null || !st.hasMoreTokens()) {
				String s = null;
				try {
					s = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (s == null)
					return false;
				st = new StringTokenizer(s);
			}
			return true;
		}

		int nextInt() {
			return Integer.parseInt(next());
		}

		long nextLong() {
			return Long.parseLong(next());
		}

		double nextDouble() {
			return Double.parseDouble(next());
		}
	}

	public static void main(String[] args) {
		new B().runIO();
	}
}