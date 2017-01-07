import java.io.*;
import java.util.*;

public class Geometry {
	FastScanner in;
	PrintWriter out;

	class Point implements Comparable<Point> {
		int x, y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}

		@Override
		public int compareTo(Point o) {
			if (x != o.x) {
				return Integer.compare(x, o.x);
			}
			return Integer.compare(y, o.y);
		}

	}

	int vectMul(Point a, Point b, Point c) {
		return Long.signum((b.x - a.x) * 1L * (c.y - a.y) - (b.y - a.y) * 1L
				* (c.x - a.x));
	}

	Point[] convexHull(Point[] pts) {
		if (pts.length <= 1) {
			return pts;
		}
		Arrays.sort(pts);
		Point first = pts[0], last = pts[pts.length - 1];
		if (first.compareTo(last) == 0) {
			return new Point[] { first };
		}
		ArrayList<Point> up = new ArrayList<>();
		ArrayList<Point> down = new ArrayList<>();
		for (Point p : pts) {
			int v = vectMul(first, last, p);
			if (v <= 0) {
				while (down.size() > 1
						&& vectMul(down.get(down.size() - 2),
								down.get(down.size() - 1), p) <= 0) {
					down.remove(down.size() - 1);
				}
				down.add(p);
			}
			if (v >= 0) {
				while (up.size() > 1
						&& vectMul(up.get(up.size() - 2),
								up.get(up.size() - 1), p) >= 0) {
					up.remove(up.size() - 1);
				}
				up.add(p);
			}
		}
		Point[] res = new Point[down.size() + up.size() - 2];
		int it = 0;
		for (int i = 0; i < down.size(); i++) {
			res[it++] = down.get(i);
		}
		Collections.reverse(up);
		for (int i = 1; i + 1 < up.size(); i++) {
			res[it++] = up.get(i);
		}
		return res;
	}

	void solve() {
		int n = in.nextInt();
		Point[] a = new Point[n];
		for (int i = 0; i < n; i++) {
			a[i] = new Point(in.nextInt(), in.nextInt());
		}
		a = convexHull(a);
		n = a.length;
		System.err.println(Arrays.toString(a));
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
		new Geometry().runIO();
	}
}