import java.io.*;
import java.util.*;

public class D {
	FastScanner in;
	PrintWriter out;

	class Point {
		int x, y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

	}

	int vectMul(Point a, Point b, Point c) {
		return Integer.signum((b.x - a.x) * (c.y - a.y) - (b.y - a.y)
				* (c.x - a.x));
	}

	void solve() {
		Point[] a = new Point[3];
		for (int i = 0; i < a.length; i++) {
			a[i] = new Point(in.nextInt(), in.nextInt());
		}
		for (int i = 0; i < a.length; i++) {
			Point p = a[i];
			for (int j = 0; j < a.length; j++) {
				if (j != i) {
					Point prev = a[j];
					for (int k = 0; k < a.length; k++) {
						if (k != i && k != j) {
							Point next = a[k];
							if (vectMul(prev, p, next) > 0) {
								if ((p.x - prev.x) * (next.x - p.x)
										+ (p.y - prev.y) * (next.y - p.y) == 0) {
									out.println((prev.x + next.x - p.x) + " "
											+ (prev.y + next.y - p.y));
									return;
								}
							}
						}
					}
				}
			}
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
		new D().runIO();
	}
}