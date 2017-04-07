import java.io.*;
import java.util.*;

public class Art10 {
	FastScanner in;
	PrintWriter out;

	char[][] rotate(char[][] a) {
		int n = a.length;
		int m = a[0].length;
		char[][] res = new char[m][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				res[m - 1 - j][i] = a[i][j];
			}
		}
		return res;
	}

	class Rectangle implements Comparable<Rectangle> {
		char[][] a;

		public Rectangle(char[][] a) {
			super();
			this.a = a;
		}

		void rot() {
			a = rotate(a);
		}

		public int compareTo(Rectangle o) {
			return Integer.compare(o.a.length * o.a[0].length, a.length
					* a[0].length);
		}

	}

	class Pos {
		int x, y;

		public Pos(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

	}

	Random rnd = new Random();

	boolean hasIntersection(int xFr, int xTo, int x2Fr, int x2To) {
		return Math.min(xTo, x2To) >= Math.max(xFr, x2Fr);
	}

	boolean hasIntersection(Rectangle a, int x, int y, Rectangle b, int x2,
			int y2) {
		return hasIntersection(x, x + a.a.length - 1, x2, x2 + b.a.length - 1)
				&& hasIntersection(y, y + a.a[0].length - 1, y2, y2
						+ b.a[0].length - 1);
	}

	char[][] getSq(Rectangle[] a, int w) {
		int[] xx = new int[a.length];
		int[] yy = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			Rectangle r = a[i];
			if (rnd.nextBoolean()) {
				r.rot();
			}
			if (r.a.length > w) {
				return null;
			}
			for (int y = 0;; y++) {
				boolean found = false;
				for (int x = 0; x + r.a.length <= w; x++) {
					boolean ok = true;
					for (int j = 0; j < i; j++) {
						if (hasIntersection(a[j], xx[j], yy[j], r, x, y)) {
							ok = false;
						}
					}
					if (ok) {
						found = true;
						xx[i] = x;
						yy[i] = y;
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
		int maxY = 0;
		for (int i = 0; i < a.length; i++) {
			maxY = Math.max(maxY, a[i].a[0].length + yy[i]);
			// System.err.println(xx[i] + " " + yy[i]);
		}
		char[][] res = new char[w][maxY];
		for (int i = 0; i < res.length; i++) {
			Arrays.fill(res[i], 'a');
		}
		for (int ii = 0; ii < a.length; ii++) {
			for (int i = 0; i < a[ii].a.length; i++) {
				for (int j = 0; j < a[ii].a[0].length; j++) {
					res[xx[ii] + i][yy[ii] + j] = a[ii].a[i][j];
				}
			}
		}
		return res;
	}

	int x;

	void solve() {
		int tc = in.nextInt();
		long sum = 0;
		int myy = 0;
		System.err.println("tests " + tc);
		for (int t = 0; t < tc; t++) {
			int cnt = in.nextInt();

			// System.err.println("do " + t + ", cnt = " + cnt);
			int totalSum = 0;
			char[][][][] a = new char[cnt][4][][];
			for (int ii = 0; ii < cnt; ii++) {
				int n = in.nextInt();
				int m = in.nextInt();
				a[ii][0] = new char[n][m];
				for (int i = 0; i < n; i++) {
					a[ii][0][i] = in.next().toCharArray();
				}
				for (int i = 1; i < 4; i++) {
					a[ii][i] = rotate(a[ii][i - 1]);
				}
				totalSum += n * m;
			}
			x = 0;
			for (int i = 0; i < cnt; i++) {
				x = Math.max(x, Math.max(a[i][0].length, a[i][0][0].length));
			}

			Rectangle[] rect = new Rectangle[cnt];
			for (int i = 0; i < cnt; i++) {
				rect[i] = new Rectangle(a[i][0]);
			}
			Arrays.sort(rect);
			char[][] now = null;
			int bW = -1;
			for (int w = 1; w < 100; w++) {
				char[][] next = getSq(rect, w);
				if (next == null) {
					continue;
				}
				if (now == null
						|| now.length * now[0].length > next.length
								* next[0].length) {
					now = next;
					bW = w;
				}
				// System.err.print(next.length * next[0].length + " ");
			}
			System.err.print(now[0].length * now.length + " ");
			for (int it = 0; it < 30; it++) {
				char[][] check = getSq(rect, bW);
				if (check.length * check[0].length < now[0].length * now.length) {
					now = check;
					System.err.print(now[0].length * now.length + " ");
				}
			}
			System.err.println();
			// System.err.println("!" + now.length * now[0].length);
			myy += now.length * now[0].length;
			char[][] res = now;
			// System.err.println("total " + totalSum);
			sum += totalSum;
			if (res.length * res[0].length > x * x * cnt) {
				System.err.println("got " + res.length + ", " + res[0].length);
				System.err.println("x = " + x + ", cnt = " + cnt);
				throw new AssertionError();
			}
			out.println(res.length + " " + res[0].length);
			for (int i = 0; i < res.length; i++) {
				for (int j = 0; j < res[i].length; j++) {
					char c = res[i][j];
					out.print(c == '.' ? 'a' : c);
				}
				out.println();
			}
			out.flush();
		}
		System.err.println("sum = " + sum + ", my = " + myy);
	}

	void run() {
		for (int test = 4; test <= 4; test++) {
			String tN = Integer.toString(test);
			while (tN.length() < 2) {
				tN = "0" + tN;
			}
			System.err.println("do " + tN);
			try {
				in = new FastScanner(new File("testy/art" + tN + ".in"));
				out = new PrintWriter(new File("testy/art" + tN + ".out"));

				solve();

				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
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
		new Art10().run();
	}
}