import java.io.*;
import java.util.*;

public class C {
	FastScanner in;
	PrintWriter out;

	class Point implements Comparable<Point> {
		int x, y, id;

		public Point(int x, int y, int id) {
			super();
			this.x = x;
			this.y = y;
			this.id = id;
		}

		public int compareTo(Point o) {
			if (x != o.x) {
				return Integer.compare(x, o.x);
			}
			return Integer.compare(y, o.y);
		}

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + ", id=" + id + "]";
		}

	}

	int vectMul(Point a, Point b, Point c) {
		return Long.signum((b.x - a.x) * 1L * (c.y - a.y) - (b.y - a.y) * 1L
				* (c.x - a.x));
	}

	boolean insideTriangle(Point p, Point a, Point b, Point c) {
		int v = Integer.signum(vectMul(a, b, p));
		if (v == 0) {
			return false;
		}
		return vectMul(b, c, p) == v && vectMul(c, a, p) == v;
	}

	boolean ask(ArrayList<Point> pts) {
		out.print("? " + pts.size());
		for (Point p : pts) {
			out.print(" " + p.id);
		}
		out.println();
		out.flush();
		return in.next().equals("Yes");
	}

	boolean ask(Point a, Point b, Point c) {
		ArrayList<Point> tmp = new ArrayList<C.Point>();
		tmp.add(a);
		tmp.add(b);
		tmp.add(c);
		return ask(tmp);
	}

	void solve() {
		int n = in.nextInt();
		final Point[] a = new Point[n];
		for (int i = 0; i < n; i++) {
			a[i] = new Point(-in.nextInt(), -in.nextInt(), i + 1);
		}
		Random rnd = new Random(123);
		Arrays.sort(a);
		Arrays.sort(a, 1, n, new Comparator<Point>() {

			public int compare(Point o1, Point o2) {
				return -vectMul(a[0], o1, o2);

			}

		});
		ArrayList<Point> convexHull = new ArrayList<C.Point>();
		for (Point p : a) {
			while (convexHull.size() >= 2) {
				Point p1 = convexHull.get(convexHull.size() - 2);
				Point p2 = convexHull.get(convexHull.size() - 1);
				if (vectMul(p1, p2, p) < 0) {
					convexHull.remove(convexHull.size() - 1);
				} else {
					break;
				}
			}
			convexHull.add(p);
		}
		int q = in.nextInt();
		for (int qq = 0; qq < q; qq++) {
			if (n <= 60) {
				while (true) {
					int i = rnd.nextInt(n);
					int j = rnd.nextInt(n);
					int k = rnd.nextInt(n);
					if (i != j && j != k && i != k) {
						boolean ok = true;
						for (int l = 0; l < n; l++) {
							if (insideTriangle(a[l], a[i], a[j], a[k])) {
								ok = false;
							}
						}
						if (ok) {
							if (ask(a[i], a[j], a[k])) {
								out.println("! 3 " + (a[i].id) + " "
										+ (a[j].id) + " " + (a[k].id));
								out.flush();
								break;
							}
						}
					}
				}
				continue;
			}
			int l = 1, r = convexHull.size() - 1;
			// point is inside 0-[l..r]-0
			while (r - l > 1) {
				int m = (l + r) >> 1;
				ArrayList<Point> check = new ArrayList<Point>();
				check.add(convexHull.get(0));
				for (int i = l; i <= m; i++) {
					check.add(convexHull.get(i));
				}
				if (ask(check)) {
					r = m;
				} else {
					l = m;
				}
			}
			Point[] inside = new Point[] { convexHull.get(0),
					convexHull.get(l), convexHull.get(r) };
			if (n <= 60) {
				for (int i = 1; i + 1 < convexHull.size(); i++) {
					if (ask(convexHull.get(0), convexHull.get(i),
							convexHull.get(i + 1))) {
						inside = new Point[] { convexHull.get(0),
								convexHull.get(i), convexHull.get(i + 1) };
					}
				}
			}
			ArrayList<Point> more = new ArrayList<C.Point>();
			for (Point p : a) {
				if (insideTriangle(p, inside[0], inside[1], inside[2])) {
					more.add(p);
				}
			}
			while (more.size() > 0) {
				Point ask = more.get(rnd.nextInt(more.size()));
				if (ask(inside[0], inside[1], ask)) {
					inside[2] = ask;
				} else {
					if (ask(inside[1], inside[2], ask)) {
						inside[0] = ask;
					} else {
						inside[1] = ask;
					}
				}
				ArrayList<Point> next = new ArrayList<C.Point>();
				for (Point p : more) {
					if (insideTriangle(p, inside[0], inside[1], inside[2])) {
						next.add(p);
					}
				}
				if (more.size() == next.size()) {
					System.exit(239);
				}
				more = next;
			}
			out.print("! 3");
			for (Point p : inside) {
				out.print(" " + p.id);
			}
			out.println();
			out.flush();
		}
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
		new C().runIO();
	}
}