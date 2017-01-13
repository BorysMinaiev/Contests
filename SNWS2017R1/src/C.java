import java.io.*;
import java.util.*;

public class C {
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
		return Long.signum((b.x - a.x) * 1L * (c.y - a.y) - (b.y - a.y) * 1L
				* (c.x - a.x));
	}

	void solve() {
		int n = in.nextInt();
		Point[] a = new Point[n];
		for (int i = 0; i < n; i++) {
			a[i] = new Point(in.nextInt(), in.nextInt());
		}
		if (n == 1) {
			out.println("yes");
		}
		Random rnd = new Random(123);
		long START = System.currentTimeMillis();
		while (System.currentTimeMillis() - START < 777) {
			int id1 = rnd.nextInt(n);
			int id2 = rnd.nextInt(n);
			if (id1 == id2) {
				continue;
			}
			int id3 = -1, id4 = -1;
			boolean ok = true;
			for (int i = 0; i < n; i++) {
				if (vectMul(a[id1], a[id2], a[i]) == 0) {
					continue;
				}
				if (id3 == -1) {
					id3 = i;
					continue;
				}
				if (id4 == -1) {
					id4 = i;
					continue;
				}
				if (vectMul(a[id3], a[id4], a[i]) == 0) {
					continue;
				}
				ok = false;
			}
			if (ok) {
				out.println("yes");
				return;
			}
		}
		out.println("no");
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